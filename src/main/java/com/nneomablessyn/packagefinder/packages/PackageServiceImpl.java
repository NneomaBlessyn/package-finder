package com.nneomablessyn.packagefinder.packages;

import com.nneomablessyn.packagefinder.commons.dto.FetchCriteria;
import com.nneomablessyn.packagefinder.commons.dto.PageableMeta;
import com.nneomablessyn.packagefinder.commons.utils.PaginationUtil;
import com.nneomablessyn.packagefinder.exceptions.BadRequestException;
import com.nneomablessyn.packagefinder.exceptions.ConflictingResourceException;
import com.nneomablessyn.packagefinder.exceptions.NotFoundException;
import com.nneomablessyn.packagefinder.packages.dto.PackageRequestDto;
import com.nneomablessyn.packagefinder.packages.dto.responses.PackageHistoryInfo;
import com.nneomablessyn.packagefinder.packages.dto.responses.PackageInfo;
import com.nneomablessyn.packagefinder.packages.dto.responses.PackageTrackingInfo;
import com.nneomablessyn.packagefinder.packages.entities.Package;
import com.nneomablessyn.packagefinder.packages.entities.PackageHistory;
import com.nneomablessyn.packagefinder.packages.enums.Status;
import com.nneomablessyn.packagefinder.packages.repositories.PackageHistoryRepository;
import com.nneomablessyn.packagefinder.packages.repositories.PackageRepository;
import com.nneomablessyn.packagefinder.packages.utils.ProxyTransformer;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PackageServiceImpl implements PackageService {

    private static final String PACKAGE_NOT_FOUND = "Package not found";

    private final PackageRepository packageRepository;
    private final PackageHistoryRepository packageHistoryRepository;

    @Override
    public PackageInfo registerPackage(PackageRequestDto request) {
        Package packageEntity = ProxyTransformer.toPackageEntity(request);
        packageEntity.setOrderId(packageRepository.generateOrderId(request.getTitle()));
        return PackageInfo.fromEntity(packageRepository.save(packageEntity));
    }

    @Override
    public PageableMeta<PackageInfo> fetchPackages(FetchCriteria fetchCriteria) {
        PageRequest pageRequest = PaginationUtil.getPageRequest(fetchCriteria.getSize(), fetchCriteria.getPage());
        Page<Package> page = packageRepository.findAll(pageRequest);
        return new PageableMeta<>(PackageInfo.fromEntities(page.getContent()), pageRequest.getPageNumber()+1, pageRequest.getPageSize(), page.getTotalElements());
    }

    @Override
    public PackageInfo fetchPackageByOrderId(String orderId) {
        return PackageInfo.fromEntity(packageRepository.findByOrderId(orderId).orElseThrow(() -> new NotFoundException(PACKAGE_NOT_FOUND)));
    }

    @Override
    public PackageInfo updatePackageStatus(String orderId, Status status) {
        if (status == null) {
            throw new BadRequestException("Status is required");
        }

        Package aPackage = packageRepository.findByOrderId(orderId)
                .orElseThrow(() -> new NotFoundException(PACKAGE_NOT_FOUND));

        if (aPackage.getStatus() == status) {
            throw new BadRequestException(String.format("Incoming status %s same as current status", status.name()));
        }

        if((status == Status.PICKED_UP || status == Status.DELIVERED) && packageHistoryRepository.existsByOrderIdAndStatus(orderId, status)){
            throw new ConflictingResourceException(String.format("Package cannot be %s more than once", status.name()));
        }

        aPackage.setStatus(status);

        //create package history for auditing and visibility
        PackageHistory packageHistory = new PackageHistory();
        packageHistory.setOrderId(orderId);
        packageHistory.setStatus(status);
        packageHistoryRepository.save(packageHistory);

        return PackageInfo.fromEntity(packageRepository.save(aPackage));
    }

    @Override
    public PackageTrackingInfo trackPackageByOrderId(String orderId) {
        Package aPackage = packageRepository.findByOrderId(orderId)
                .orElseThrow(() -> new NotFoundException(PACKAGE_NOT_FOUND));
        if(aPackage.getStatus() == null){
            throw new NotFoundException("Package not picked up");
        }
        return new PackageTrackingInfo(aPackage.getTitle(), aPackage.getOrderId(), aPackage.getStatus());
    }

    @Override
    public PageableMeta<PackageHistoryInfo> fetchPackageHistoryByOrderId(String orderId, FetchCriteria fetchCriteria) {
        if(!packageRepository.existsByOrderId(orderId)){
            throw new NotFoundException(PACKAGE_NOT_FOUND);
        }
        PageRequest pageRequest = PaginationUtil.getPageRequest(fetchCriteria.getSize(), fetchCriteria.getPage());
        Page<PackageHistory> page = packageHistoryRepository.findAllByOrderId(pageRequest, orderId);
        return new PageableMeta<>(PackageHistoryInfo.fromEntities(page.getContent()), pageRequest.getPageNumber()+1, pageRequest.getPageSize(), page.getTotalElements());
    }
}

package com.nneomablessyn.packagefinder.packages;

import com.nneomablessyn.packagefinder.commons.dto.FetchCriteria;
import com.nneomablessyn.packagefinder.commons.dto.PageableMeta;
import com.nneomablessyn.packagefinder.packages.dto.PackageRequestDto;
import com.nneomablessyn.packagefinder.packages.dto.responses.PackageHistoryInfo;
import com.nneomablessyn.packagefinder.packages.dto.responses.PackageInfo;
import com.nneomablessyn.packagefinder.packages.dto.responses.PackageTrackingInfo;
import com.nneomablessyn.packagefinder.packages.enums.Status;

public interface PackageService {
    PackageInfo registerPackage(PackageRequestDto request);

    PageableMeta<PackageInfo> fetchPackages(FetchCriteria fetchCriteria);

    PackageInfo fetchPackageByOrderId(String orderId);

    PackageInfo updatePackageStatus(String orderId, Status status);

    PackageTrackingInfo trackPackageByOrderId(String orderId);

    PageableMeta<PackageHistoryInfo> fetchPackageHistoryByOrderId(String orderId, FetchCriteria fetchCriteria);
}

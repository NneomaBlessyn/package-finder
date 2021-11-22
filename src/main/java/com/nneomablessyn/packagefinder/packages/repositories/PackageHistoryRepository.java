package com.nneomablessyn.packagefinder.packages.repositories;

import com.nneomablessyn.packagefinder.packages.entities.PackageHistory;
import com.nneomablessyn.packagefinder.packages.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PackageHistoryRepository extends JpaRepository<PackageHistory, Long>{

    Page<PackageHistory> findAllByOrderId(Pageable pageable, @Param("orderId") String orderId);

    boolean existsByOrderIdAndStatus(@Param("orderId") String orderId, @Param("status") Status status);
}

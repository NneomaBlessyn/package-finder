package com.nneomablessyn.packagefinder.packages.repositories;

import com.nneomablessyn.packagefinder.commons.utils.IdGenerator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.nneomablessyn.packagefinder.packages.entities.Package;

import java.util.Optional;

@Repository
public interface PackageRepository extends JpaRepository<Package, Long>, JpaSpecificationExecutor<Package> {

    default String generateOrderId(String name) {
        String orderId;
        boolean orderIdExists;
        do {
            orderId = (IdGenerator.generatePackageCode(name, 3) + IdGenerator.randomCode(4)).toUpperCase();
            orderIdExists = existsByOrderId(orderId);
        } while (orderIdExists);
        return orderId;
    }

    boolean existsByOrderId(@Param("orderId") String orderId);

    Optional<Package> findByOrderId(@Param("orderId") String orderId);
}

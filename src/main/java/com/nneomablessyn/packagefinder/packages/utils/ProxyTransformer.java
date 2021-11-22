package com.nneomablessyn.packagefinder.packages.utils;

import com.nneomablessyn.packagefinder.packages.dto.PackageRequestDto;
import com.nneomablessyn.packagefinder.packages.entities.Package;

public final class ProxyTransformer {

    ProxyTransformer(){}


    public static Package toPackageEntity(PackageRequestDto request) {
        Package packageEntity = new Package();
        packageEntity.setTitle(request.getTitle());
        packageEntity.setDescription(request.getDescription());
        packageEntity.setPickupAddress(request.getPickupAddress());
        packageEntity.setDeliveryAddress(request.getDeliveryAddress());
        packageEntity.setSenderName(request.getSenderName());
        packageEntity.setSenderPhoneNumber(request.getSenderPhoneNumber());
        packageEntity.setRecipientName(request.getRecipientName());
        packageEntity.setRecipientPhoneNumber(request.getRecipientPhoneNumber());
        return packageEntity;
    }
}

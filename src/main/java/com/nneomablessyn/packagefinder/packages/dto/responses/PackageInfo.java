package com.nneomablessyn.packagefinder.packages.dto.responses;

import com.nneomablessyn.packagefinder.packages.entities.Package;
import com.nneomablessyn.packagefinder.packages.enums.Status;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@Data
public class PackageInfo {

    private Long id;

    private String title;

    private String description;

    private String pickupAddress;

    private String deliveryAddress;

    private String senderName;

    private String senderPhoneNumber;

    private String recipientName;

    private String recipientPhoneNumber;

    private Status status;

    private String orderId;

    private Instant createdAt;

    public static PackageInfo fromEntity(Package packageEntity) {
        if (packageEntity == null) {
            return null;
        }
        return PackageInfo.builder()
                .id(packageEntity.getId())
                .title(packageEntity.getTitle())
                .description(packageEntity.getDescription())
                .pickupAddress(packageEntity.getPickupAddress())
                .deliveryAddress(packageEntity.getDeliveryAddress())
                .senderName(packageEntity.getSenderName())
                .senderPhoneNumber(packageEntity.getSenderPhoneNumber())
                .recipientName(packageEntity.getRecipientName())
                .recipientPhoneNumber(packageEntity.getRecipientPhoneNumber())
                .status(packageEntity.getStatus())
                .orderId(packageEntity.getOrderId())
                .createdAt(packageEntity.getCreatedAt())
                .build();
    }

    public static List<PackageInfo> fromEntities(List<Package> packageList) {
        if (packageList == null || packageList.isEmpty()) {
            return Collections.emptyList();
        }
        return packageList.stream().map(PackageInfo::fromEntity).collect(Collectors.toList());
    }
}

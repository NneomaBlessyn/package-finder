package com.nneomablessyn.packagefinder.packages.dto.responses;

import com.nneomablessyn.packagefinder.packages.entities.PackageHistory;
import com.nneomablessyn.packagefinder.packages.enums.Status;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class PackageHistoryInfo {

    private Long id;

    private Status status;

    private String orderId;

    private Instant createdAt;

    public static PackageHistoryInfo fromEntity(PackageHistory packageHistory) {
        if (packageHistory == null) {
            return null;
        }
        return PackageHistoryInfo.builder()
                .id(packageHistory.getId())
                .status(packageHistory.getStatus())
                .orderId(packageHistory.getOrderId())
                .createdAt(packageHistory.getCreatedAt())
                .build();
    }

    public static List<PackageHistoryInfo> fromEntities(List<PackageHistory> packageHistories) {
        if (packageHistories == null || packageHistories.isEmpty()) {
            return Collections.emptyList();
        }
        return packageHistories.stream().map(PackageHistoryInfo::fromEntity).collect(Collectors.toList());
    }
}

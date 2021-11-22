package com.nneomablessyn.packagefinder.packages.dto.responses;

import com.nneomablessyn.packagefinder.packages.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PackageTrackingInfo {
    private String title;

    private String orderId;

    private Status status;
}

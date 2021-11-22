package com.nneomablessyn.packagefinder.controllers;


import com.nneomablessyn.packagefinder.commons.dto.FetchCriteria;
import com.nneomablessyn.packagefinder.commons.dto.PageableMeta;
import com.nneomablessyn.packagefinder.commons.dto.Response;
import com.nneomablessyn.packagefinder.packages.PackageService;
import com.nneomablessyn.packagefinder.packages.dto.PackageRequestDto;
import com.nneomablessyn.packagefinder.packages.dto.responses.PackageHistoryInfo;
import com.nneomablessyn.packagefinder.packages.dto.responses.PackageInfo;
import com.nneomablessyn.packagefinder.packages.dto.responses.PackageTrackingInfo;
import com.nneomablessyn.packagefinder.packages.enums.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("v1/package")
@RequiredArgsConstructor
public class PackageController {

    private final PackageService packageService;

    @PostMapping("")
    public ResponseEntity<Response<PackageInfo>> registerPackage(@Valid @NotNull(message = "Request body cannot be null")
                                                                                 @RequestBody PackageRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(new Response<>(true, "Package registered successfully", packageService.registerPackage(request)));
    }

    @GetMapping("")
    public ResponseEntity<Response<PageableMeta<PackageInfo>>> fetchPackages(@Valid FetchCriteria fetchCriteria) {
        return ResponseEntity.status(HttpStatus.OK).body(new Response<>(true, "Packages fetched successfully", packageService.fetchPackages(fetchCriteria)));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Response<PackageInfo>> fetchPackageByOrderId(@PathVariable String orderId) {
        return ResponseEntity.status(HttpStatus.OK).body(new Response<>(true, "Package fetched successfully", packageService.fetchPackageByOrderId(orderId)));
    }

    @PutMapping("/status/update")
    public ResponseEntity<Response<PackageInfo>> updatePackageStatus(@RequestParam("orderId") String orderId, @RequestParam("status") final Status status) {
        return ResponseEntity.status(HttpStatus.OK).body(new Response<>(true, "Package status updated successfully", packageService.updatePackageStatus(orderId, status)));
    }

    @GetMapping("/track")
    public ResponseEntity<Response<PackageTrackingInfo>> trackPackageByOrderId(@RequestParam("orderId")  String orderId) {
        return ResponseEntity.status(HttpStatus.OK).body(new Response<>(true, "Package status fetched successfully", packageService.trackPackageByOrderId(orderId)));
    }

    @GetMapping("/history")
    public ResponseEntity<Response<PageableMeta<PackageHistoryInfo>>> fetchPackageHistoryByOrderId(@RequestParam("orderId")  String orderId, @Valid FetchCriteria fetchCriteria) {
        return ResponseEntity.status(HttpStatus.OK).body(new Response<>(true, "Package history fetched successfully", packageService.fetchPackageHistoryByOrderId(orderId, fetchCriteria)));
    }

}

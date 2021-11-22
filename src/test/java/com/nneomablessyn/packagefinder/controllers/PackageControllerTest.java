package com.nneomablessyn.packagefinder.controllers;

import com.nneomablessyn.packagefinder.commons.dto.FetchCriteria;
import com.nneomablessyn.packagefinder.commons.dto.PageableMeta;
import com.nneomablessyn.packagefinder.commons.dto.Response;
import com.nneomablessyn.packagefinder.exceptions.BadRequestException;
import com.nneomablessyn.packagefinder.exceptions.ConflictingResourceException;
import com.nneomablessyn.packagefinder.exceptions.NotFoundException;
import com.nneomablessyn.packagefinder.packages.PackageService;
import com.nneomablessyn.packagefinder.packages.PackageServiceImpl;
import com.nneomablessyn.packagefinder.packages.dto.PackageRequestDto;
import com.nneomablessyn.packagefinder.packages.dto.responses.PackageHistoryInfo;
import com.nneomablessyn.packagefinder.packages.dto.responses.PackageInfo;
import com.nneomablessyn.packagefinder.packages.dto.responses.PackageTrackingInfo;
import com.nneomablessyn.packagefinder.packages.entities.Package;
import com.nneomablessyn.packagefinder.packages.enums.Status;
import com.nneomablessyn.packagefinder.packages.repositories.PackageHistoryRepository;
import com.nneomablessyn.packagefinder.packages.repositories.PackageRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PackageControllerTest {

    PackageController packageController;

    PackageService packageService;

    PackageRepository packageRepository = mock(PackageRepository.class);

    PackageHistoryRepository packageHistoryRepository = mock(PackageHistoryRepository.class);

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    private final List<String> errorMessages = Arrays.asList(
            "Item title is required",
            "Pickup address is required",
            "Delivery address is required",
            "Sender phone number is not valid",
            "Recipient phone number is required",
            "Recipient phone number is not valid"
    );

    @BeforeEach
    void setUp(){
        packageService = new PackageServiceImpl(packageRepository, packageHistoryRepository);
        packageController = new PackageController(packageService);
    }

    @Test
    @DisplayName("Register package request without required fields fails validation")
    void registerPackageRequestWithoutRequiredFieldsFailsValidation() {
        PackageRequestDto request = new PackageRequestDto();
        Set<ConstraintViolation<PackageRequestDto>> violations = validator.validate(request);
        assertNotNull(violations);
        violations.forEach(violation -> assertTrue(errorMessages.contains(violation.getMessage())));
    }

    @Test
    void registerPackage() {
        PackageRequestDto request = new PackageRequestDto();
        request.setTitle("Charger");

        when(packageRepository.generateOrderId(ArgumentMatchers.any())).thenReturn("OBC123G");
        when(packageRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        ResponseEntity<Response<PackageInfo>> response = packageController.registerPackage(request);

        Assertions.assertNotNull(response);
        Response<PackageInfo> responseBody = response.getBody();
        Assertions.assertNotNull(responseBody);
        Assertions.assertEquals("Package registered successfully", responseBody.getMessage());
        Assertions.assertNotNull(responseBody.getData());
        Assertions.assertNotNull(responseBody.getData().getOrderId());
        Assertions.assertFalse(responseBody.getData().getOrderId().isEmpty());
    }

    @Test
    void fetchPackages() {

        when(packageRepository.findAll((Pageable) ArgumentMatchers.any())).thenReturn(Page.empty());

        ResponseEntity<Response<PageableMeta<PackageInfo>>> response = packageController.fetchPackages(new FetchCriteria());

        Assertions.assertNotNull(response);
        Response<PageableMeta<PackageInfo>> responseBody = response.getBody();
        Assertions.assertNotNull(responseBody);
        Assertions.assertEquals("Packages fetched successfully", responseBody.getMessage());
        Assertions.assertNotNull(responseBody.getData());
    }

    @Test
    @DisplayName("Fetch package for unknown order ID throws NotFoundException")
    void fetchPackageByOrderIdForUnknownOrderIdThrowsNotFound(){

        when(packageRepository.findByOrderId(ArgumentMatchers.any())).thenReturn(Optional.empty());

        Exception exception = null;
        try {
            packageController.fetchPackageByOrderId("OBC123G");
        }catch (Exception ex){
            exception = ex;
        }

        Assertions.assertNotNull(exception);
        Assertions.assertTrue(exception instanceof NotFoundException);
        Assertions.assertEquals("Package not found", exception.getMessage());
    }

    @Test
    void fetchPackageByOrderId() {

        when(packageRepository.findByOrderId(ArgumentMatchers.any())).thenReturn(Optional.of(new Package()));

        ResponseEntity<Response<PackageInfo>> response = packageController.fetchPackageByOrderId("OBC123G");

        Assertions.assertNotNull(response);
        Response<PackageInfo> responseBody = response.getBody();
        Assertions.assertNotNull(responseBody);
        Assertions.assertEquals("Package fetched successfully", responseBody.getMessage());
        Assertions.assertNotNull(responseBody.getData());
    }

    @Test
    @DisplayName("Update package status for null status throws BadRequestException")
    void updatePackageStatusForNullStatusThrowsBadRequest() {

        Exception exception = null;
        try {
            packageController.updatePackageStatus("OBC123G", null);
        }catch (Exception e){
            exception = e;
        }
        Assertions.assertNotNull(exception);
        Assertions.assertTrue(exception instanceof BadRequestException);
        Assertions.assertEquals("Status is required", exception.getLocalizedMessage());
    }

    @Test
    @DisplayName("Update package status for unknown order ID throws NotFoundException")
    void updatePackageStatusForUnknownOrderIdThrowsNotFound() {

        when(packageRepository.findByOrderId(any())).thenReturn(Optional.empty());

        Exception exception = null;
        try {
            packageController.updatePackageStatus("OBC123G", Status.PICKED_UP);
        }catch (Exception e){
            exception = e;
        }
        Assertions.assertNotNull(exception);
        Assertions.assertTrue(exception instanceof NotFoundException);
        Assertions.assertEquals("Package not found", exception.getLocalizedMessage());
    }

    @Test
    @DisplayName("Update package status for same status throws BadRequestException")
    void updatePackageStatusForSameStatusThrowsBadRequest() {
        Status status = Status.WAREHOUSE;

        Package aPackage = new Package();
        aPackage.setStatus(status);
        when(packageRepository.findByOrderId(any())).thenReturn(Optional.of(aPackage));
        when(packageHistoryRepository.existsByOrderIdAndStatus("OBC123G", status)).thenReturn(true);

        Exception exception = null;
        try {
            packageController.updatePackageStatus("OBC123G", status);
        }catch (Exception e){
            exception = e;
        }
        Assertions.assertNotNull(exception);
        Assertions.assertTrue(exception instanceof BadRequestException);
        Assertions.assertEquals("Incoming status "+ status.name() +" same as current status", exception.getLocalizedMessage());
    }

    @Test
    @DisplayName("Update package status to PICKED_UP or DELIVERED for already picked up or delivered package throws ConflictingResourceException")
    void updatePackageStatusForAlreadyPickedUpOrDeliveredPackageThrowsConflictingResource() {
        Status status = Status.PICKED_UP;

        when(packageRepository.findByOrderId(any())).thenReturn(Optional.of(new Package()));
        when(packageHistoryRepository.existsByOrderIdAndStatus("OBC123G", status)).thenReturn(true);

        Exception exception = null;
        try {
            packageController.updatePackageStatus("OBC123G", status);
        }catch (Exception e){
            exception = e;
        }
        Assertions.assertNotNull(exception);
        Assertions.assertTrue(exception instanceof ConflictingResourceException);
        Assertions.assertEquals("Package cannot be "+ status.name()+" more than once", exception.getLocalizedMessage());
    }

    @Test
    @DisplayName("Update package status for valid request is successful")
    void updatePackageStatus() {

        when(packageRepository.findByOrderId(any())).thenReturn(Optional.of(new Package()));
        when(packageRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);
        when(packageHistoryRepository.existsByOrderIdAndStatus("OBC123G", Status.PICKED_UP)).thenReturn(false);

        ResponseEntity<Response<PackageInfo>> response = packageController.updatePackageStatus("OBC123G", Status.PICKED_UP);


        Assertions.assertNotNull(response);
        Response<PackageInfo> responseBody = response.getBody();
        Assertions.assertNotNull(responseBody);
        Assertions.assertEquals("Package status updated successfully", responseBody.getMessage());
        Assertions.assertNotNull(responseBody.getData());
        verify(packageHistoryRepository, times(1)).save(any());

    }

    @Test
    @DisplayName("Track package for unknown order ID throws NotFoundException")
    void trackPackageByOrderIdForUnknownOrderIdThrowsNotFound() {

        when(packageRepository.findByOrderId(any())).thenReturn(Optional.empty());

        Exception exception = null;
        try {
            packageController.trackPackageByOrderId("OBC123G");
        }catch (Exception e){
            exception = e;
        }
        Assertions.assertNotNull(exception);
        Assertions.assertTrue(exception instanceof NotFoundException);
        Assertions.assertEquals("Package not found", exception.getLocalizedMessage());
    }

    @Test
    @DisplayName("Track package for null status throws NotFoundException")
    void trackPackageByOrderIdForUnknownNullStatusThrowsNotFound() {

        when(packageRepository.findByOrderId(any())).thenReturn(Optional.of(new Package()));

        Exception exception = null;
        try {
            packageController.trackPackageByOrderId("OBC123G");
        }catch (Exception e){
            exception = e;
        }
        Assertions.assertNotNull(exception);
        Assertions.assertTrue(exception instanceof NotFoundException);
        Assertions.assertEquals("Package not picked up", exception.getLocalizedMessage());
    }

    @Test
    void trackPackageByOrderId() {

        Package aPackage = new Package();
        aPackage.setStatus(Status.PICKED_UP);
        when(packageRepository.findByOrderId(any())).thenReturn(Optional.of(aPackage));

        ResponseEntity<Response<PackageTrackingInfo>> response = packageController.trackPackageByOrderId("OBC123G");

        Assertions.assertNotNull(response);
        Response<PackageTrackingInfo> responseBody = response.getBody();
        Assertions.assertNotNull(responseBody);
        Assertions.assertEquals("Package status fetched successfully", responseBody.getMessage());
        Assertions.assertNotNull(responseBody.getData());
    }

    @Test
    @DisplayName("Fetch package history for unknown order ID throws NotFoundException")
    void fetchPackageHistoryByOrderIdForUnknownOrderIdThrowsNotFound() {

        when(packageRepository.existsByOrderId(any())).thenReturn(false);

        Exception exception = null;
        try {
            packageController.fetchPackageHistoryByOrderId("OBC123G", new FetchCriteria());
        }catch (Exception e){
            exception = e;
        }
        Assertions.assertNotNull(exception);
        Assertions.assertTrue(exception instanceof NotFoundException);
        Assertions.assertEquals("Package not found", exception.getLocalizedMessage());
    }

    @Test
    void fetchPackageHistoryByOrderId() {
        when(packageRepository.existsByOrderId(any())).thenReturn(true);

        when(packageHistoryRepository.findAllByOrderId(ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(Page.empty());

        ResponseEntity<Response<PageableMeta<PackageHistoryInfo>>> response = packageController.fetchPackageHistoryByOrderId("OBC123G", new FetchCriteria());

        Assertions.assertNotNull(response);
        Response<PageableMeta<PackageHistoryInfo>> responseBody = response.getBody();
        Assertions.assertNotNull(responseBody);
        Assertions.assertEquals("Package history fetched successfully", responseBody.getMessage());
        Assertions.assertNotNull(responseBody.getData());
    }
}
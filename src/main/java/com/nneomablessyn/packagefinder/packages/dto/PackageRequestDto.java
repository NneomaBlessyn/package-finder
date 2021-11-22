package com.nneomablessyn.packagefinder.packages.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class PackageRequestDto {

    @NotBlank(message = "Item title is required")
    private String title;

    private String description;

    @NotBlank(message = "Pickup address is required")
    private String pickupAddress;

    @NotBlank(message = "Delivery address is required")
    private String deliveryAddress;

    private String senderName;

    @Pattern(regexp="(\\+234|0|234)[0-9]{10}", message = "Sender phone number is not valid")
    private String senderPhoneNumber;

    private String recipientName;

    @NotBlank(message = "Recipient phone number is required")
    @Pattern(regexp="(\\+234|0|234)[0-9]{10}", message = "Recipient phone number is not valid")
    private String recipientPhoneNumber;
}

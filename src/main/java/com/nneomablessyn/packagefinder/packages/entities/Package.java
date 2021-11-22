package com.nneomablessyn.packagefinder.packages.entities;

import com.nneomablessyn.packagefinder.commons.entities.BaseMutableEntity;
import com.nneomablessyn.packagefinder.packages.enums.Status;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

@Entity
@Table(name = "package")
@Getter
@Setter
public class Package extends BaseMutableEntity {

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "pickup_address", nullable = false)
    private String pickupAddress;

    @Column(name = "delivery_address", nullable = false)
    private String deliveryAddress;

    @Column(name = "sender_name")
    private String senderName;

    @Column(name = "sender_phone_number")
    private String senderPhoneNumber;

    @Column(name = "recipient_name")
    private String recipientName;

    @Column(name = "recipient_phone_number", nullable = false)
    private String recipientPhoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(name="status")
    private Status status;

    @Column(name = "order_id", nullable = false, unique = true)
    private String orderId;
}

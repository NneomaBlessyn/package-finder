package com.nneomablessyn.packagefinder.packages.entities;

import com.nneomablessyn.packagefinder.commons.entities.BaseEntity;
import com.nneomablessyn.packagefinder.packages.enums.Status;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

@Entity
@Table(name = "package_history")
@Getter
@Setter
public class PackageHistory extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(name="status", nullable = false)
    private Status status;

    @Column(name = "order_id", nullable = false)
    private String orderId;
}

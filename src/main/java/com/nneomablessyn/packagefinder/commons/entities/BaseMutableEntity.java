package com.nneomablessyn.packagefinder.commons.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.Instant;

/**
 * This class is for OOP inheritance only, it serves as a base class from which common properties
 * can be derived from for your entities. Its merely for convenience so common fields don't get duplicated
 * in entity classes
 */
@Setter
@Getter
@MappedSuperclass
public class BaseMutableEntity extends BaseEntity {

    @Column(name = "updatedAt", nullable = false)
    private Instant updatedAt;

    @Override
    @PrePersist
    @PreUpdate
    public void prePersist() {
        if (createdAt == null) {
            createdAt = Instant.now();
        }
        updatedAt = Instant.now();
    }

}

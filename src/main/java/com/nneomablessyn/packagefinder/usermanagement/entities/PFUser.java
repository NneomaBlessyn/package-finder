package com.nneomablessyn.packagefinder.usermanagement.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nneomablessyn.packagefinder.commons.entities.BaseMutableEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(
        name = "pf_user", uniqueConstraints =
        {@UniqueConstraint(columnNames = {"email"}), @UniqueConstraint(columnNames = {"phoneNumber"})},
        indexes = {@Index(columnList = "fullName, email")}
)
public class PFUser extends BaseMutableEntity {

    private static final long serialVersionUID = 203663209042887525L;

    @Email(flags = Pattern.Flag.CASE_INSENSITIVE)
    private String email;

    private String phoneNumber;

    private String fullName;

    @JsonIgnore
    private String password;

    private LocalDateTime lastPasswordChange;

    @Column
    private boolean locked;
}

package com.nneomablessyn.packagefinder.usermanagement.dto.response;

import com.nneomablessyn.packagefinder.usermanagement.entities.PFUser;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserInfo {

    private Long id;

    private String email;

    private String phoneNumber;

    private String fullName;

    public static UserInfo fromEntity(PFUser user) {
        if(user == null){
            return null;
        }
        return UserInfo.builder()
                .id(user.getId())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .fullName(user.getFullName())
                .build();
    }
}

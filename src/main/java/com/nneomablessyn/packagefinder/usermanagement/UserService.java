package com.nneomablessyn.packagefinder.usermanagement;

import com.nneomablessyn.packagefinder.usermanagement.dto.LoginRequest;
import com.nneomablessyn.packagefinder.usermanagement.dto.UserRequestDto;
import com.nneomablessyn.packagefinder.usermanagement.dto.response.UserInfo;
import com.nneomablessyn.packagefinder.usermanagement.entities.PFUser;

import java.util.List;

public interface UserService {

    UserInfo create(UserRequestDto request) ;

    List<Object> login(LoginRequest request);
}

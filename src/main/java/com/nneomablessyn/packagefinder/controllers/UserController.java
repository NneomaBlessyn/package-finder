package com.nneomablessyn.packagefinder.controllers;

import com.nneomablessyn.packagefinder.commons.dto.Response;
import com.nneomablessyn.packagefinder.usermanagement.UserService;
import com.nneomablessyn.packagefinder.usermanagement.dto.LoginRequest;
import com.nneomablessyn.packagefinder.usermanagement.dto.UserRequestDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping
    public ResponseEntity<Object> createUser(@RequestBody @Valid UserRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(new Response<>(true, "User created successfully", userService.create(request)));
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(new Response<>(true, "User login successfully", userService.login(loginRequest)));
    }
}

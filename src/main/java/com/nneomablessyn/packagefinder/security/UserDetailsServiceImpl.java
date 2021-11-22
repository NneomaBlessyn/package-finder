package com.nneomablessyn.packagefinder.security;

import com.nneomablessyn.packagefinder.exceptions.CustomException;
import com.nneomablessyn.packagefinder.usermanagement.dto.LoginRequest;
import com.nneomablessyn.packagefinder.usermanagement.entities.PFUser;
import com.nneomablessyn.packagefinder.usermanagement.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService, ApplicationListener<AuthenticationSuccessEvent> {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        final PFUser user = userRepository.findByEmailIgnoreCase(email).orElseThrow(() -> new CustomException(String.format("User %s Not Found", email), HttpStatus.NOT_FOUND));
        return new UserDetailsImpl(user);
    }

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        // save user event on db
    }

    public PFUser loadUserByLoginCommand(LoginRequest loginRequest) {
        PFUser user = userRepository.findByEmailIgnoreCase(loginRequest.getEmail()).orElseThrow(() -> new CustomException("User Not Found", HttpStatus.NOT_FOUND));
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), loginRequest.getPassword()));
        return user;
    }
}

package com.nneomablessyn.packagefinder.usermanagement;

import com.nneomablessyn.packagefinder.security.UserDetailsServiceImpl;
import com.nneomablessyn.packagefinder.security.auth.JwtTokenProvider;
import com.nneomablessyn.packagefinder.usermanagement.dto.LoginRequest;
import com.nneomablessyn.packagefinder.usermanagement.dto.UserRequestDto;
import com.nneomablessyn.packagefinder.usermanagement.dto.response.UserInfo;
import com.nneomablessyn.packagefinder.usermanagement.entities.PFUser;
import com.nneomablessyn.packagefinder.usermanagement.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder psdEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsServiceImpl userDetailsService;

    @Transactional
    @Override
    public UserInfo create(UserRequestDto request) {
        PFUser newUser = new PFUser();
        newUser.setEmail(request.getEmail());
        newUser.setPhoneNumber(request.getPhoneNumber());
        newUser.setFullName(request.getFullName());
        newUser.setPassword(psdEncoder.encode(String.valueOf(request.getPassword())));
        return UserInfo.fromEntity(userRepository.save(newUser));
    }

    @Override
    public List<Object> login(LoginRequest request) {
        PFUser user = userDetailsService.loadUserByLoginCommand(request);
        String token = jwtTokenProvider.createToken(user.getEmail(), false);
        return Arrays.asList(UserInfo.fromEntity(user), token);
    }
}

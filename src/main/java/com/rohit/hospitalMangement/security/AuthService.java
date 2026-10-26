package com.rohit.hospitalMangement.security;
import org.springframework.stereotype.Service;

import com.rohit.hospitalMangement.DTO.LoginRequest;
import com.rohit.hospitalMangement.DTO.LoginResponse;
import com.rohit.hospitalMangement.Entity.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthService {


    private final AuthenticationManager authenticationManager;
    private final AuthUtils authUtils;


    public LoginResponse login(LoginRequest loginRequest) {

        // validate this user also check this use have token already or not .
        Authentication authentication = authenticationManager.authenticate(
                                     new UsernamePasswordAuthenticationToken(
                                      loginRequest.getUsername(),
                                      loginRequest.getPassword()
                                      )
                                    );

        Users user = (Users)authentication.getPrincipal();
        String token = authUtils.generateToken(user, user.getRole());

        return LoginResponse.builder()
                .token(token)
                .type("Bearer")
                .username(user.getUsername())
                .role(user.getRole())
                .build();

    }
}

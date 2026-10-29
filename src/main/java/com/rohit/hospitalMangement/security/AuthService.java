package com.rohit.hospitalMangement.security;

import com.rohit.hospitalMangement.DTO.LoginRequest;
import com.rohit.hospitalMangement.DTO.LoginResponse;
import com.rohit.hospitalMangement.DTO.SignUpRequest;
import com.rohit.hospitalMangement.DTO.SignUpResponse;
import com.rohit.hospitalMangement.Entity.Users;
import com.rohit.hospitalMangement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final AuthUtils authUtils;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public LoginResponse login(LoginRequest loginRequest) {
        // validate this user also check this use have token already or not .
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        Users user = (Users) authentication.getPrincipal();
        String token = authUtils.generateToken(user, user.getRole());

        return LoginResponse.builder()
                .token(token)
                .type("Bearer")
                .username(user.getUsername())
                .role(user.getRole())
                .build();
    }

    public SignUpResponse signup(SignUpRequest signUpRequest) {
        if (signUpRequest == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request body cannot be null");
        }

        String username = signUpRequest.getUsername();
        String password = signUpRequest.getPassword();

        if (username == null || username.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username is required");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password is required");
        }

        username = username.trim();

        if (userRepository.existsByUsername(username)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username '" + username + "' is already taken");
        }

        String role = signUpRequest.getRole();
        if (role == null || role.trim().isEmpty()) {
            role = "PATIENT";
        } else {
            role = role.trim().toUpperCase();
            if (role.startsWith("ROLE_")) {
                role = role.substring(5);
            }
        }

        Users user = Users.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .role(role)
                .build();

        Users savedUser = userRepository.save(user);

        String token = authUtils.generateToken(savedUser, savedUser.getRole());

        return SignUpResponse.builder()
                .id(savedUser.getId())
                .username(savedUser.getUsername())
                .role(savedUser.getRole())
                .token(token)
                .type("Bearer")
                .message("User registered successfully")
                .build();
    }
}

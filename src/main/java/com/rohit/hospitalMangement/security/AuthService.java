package com.rohit.hospitalMangement.security;

import com.rohit.hospitalMangement.DTO.LoginRequest;
import com.rohit.hospitalMangement.DTO.LoginResponse;
import com.rohit.hospitalMangement.DTO.SignUpRequest;
import com.rohit.hospitalMangement.DTO.SignUpResponse;
import com.rohit.hospitalMangement.Entity.Users;
import com.rohit.hospitalMangement.Entity.type.ProviderType;
import com.rohit.hospitalMangement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
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

    public LoginResponse handleOAuthLoginRequest(OAuth2User oAuth2User, String registrationId) {

        // Determine the OAuth2 provider type (GOOGLE, GITHUB, FACEBOOK etc.)
        ProviderType providerType = authUtils.getProviderTypeFromProviderId(registrationId);
        log.info("[OAuth2] provider={}, attributes={}", registrationId, oAuth2User.getAttributes().keySet());

        // Extract the unique provider ID for this user (e.g. Google "sub" or GitHub "id")
        String providerId = authUtils.determineProviderIdFromOAuthUser(oAuth2User, registrationId);
        log.info("[OAuth2] providerId={}", providerId);

        // Determine the best username: prefer email, fall back to login/sub/providerId
        String username = authUtils.determineUserNameFromOAuth2User(oAuth2User, registrationId, providerId);
        log.info("[OAuth2] resolved username={}", username);

        String email = oAuth2User.getAttribute("email");
        log.info("[OAuth2] email from provider={}", email);

        // Case 1: User already registered with this OAuth provider — just log them in
        Users user = userRepository.findByProviderIdAndProviderType(providerId, providerType).orElse(null);

        if (user != null) {
            log.info("[OAuth2] existing user found id={}, username={}", user.getId(), user.getUsername());
            // Keep username in sync if the email changed on the provider side
            if (email != null && !email.isBlank() && !email.equals(user.getUsername())) {
                user.setUsername(email);
                userRepository.save(user);
            }

        } else {
            // Case 2: A local account already exists with this email — block the OAuth login
            //         to prevent account hijacking via a different OAuth provider.
            boolean localAccountExists = email != null && userRepository.existsByUsername(email);
            if (localAccountExists) {
                log.warn("[OAuth2] login blocked — local account already exists for email={}", email);
                throw new BadCredentialsException(
                        "An account already exists with email: " + email
                        + ". Please log in with your original method (username/password)."
                );
            }

            // Case 3: Brand-new user — register them via OAuth
            log.info("[OAuth2] creating new user username={}, provider={}", username, providerType);
            user = Users.builder()
                    .username(username)
                    .password(null)          // OAuth users have no local password
                    .role("PATIENT")         // Default role for OAuth sign-ups
                    .providerId(providerId)
                    .providerType(providerType)
                    .build();
            user = userRepository.save(user);
            log.info("[OAuth2] new user saved id={}", user.getId());
        }

        // Generate a JWT for the authenticated OAuth user
        String token = authUtils.generateToken(user, user.getRole());
        log.info("[OAuth2] JWT issued for username={}", user.getUsername());

        return LoginResponse.builder()
                .token(token)
                .type("Bearer")
                .username(user.getUsername())
                .role(user.getRole())
                .build();
    }
}

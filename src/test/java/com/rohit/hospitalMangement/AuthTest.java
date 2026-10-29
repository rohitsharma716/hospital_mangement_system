package com.rohit.hospitalMangement;

import com.rohit.hospitalMangement.DTO.LoginRequest;
import com.rohit.hospitalMangement.DTO.LoginResponse;
import com.rohit.hospitalMangement.DTO.SignUpRequest;
import com.rohit.hospitalMangement.DTO.SignUpResponse;
import com.rohit.hospitalMangement.security.AuthService;
import com.rohit.hospitalMangement.security.AuthUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.server.ResponseStatusException;

@SpringBootTest
public class AuthTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private AuthUtils authUtils;

    @Autowired
    private UserDetailsService userDetailsService;

    @Test
    public void testSuccessfulSignUpAndLogin() {
        String testUser = "newuser_" + System.currentTimeMillis();
        SignUpRequest request = SignUpRequest.builder()
                .username(testUser)
                .password("password123")
                .role("PATIENT")
                .build();

        SignUpResponse signUpResponse = authService.signup(request);

        Assertions.assertNotNull(signUpResponse);
        Assertions.assertNotNull(signUpResponse.getId());
        Assertions.assertEquals(testUser, signUpResponse.getUsername());
        Assertions.assertEquals("PATIENT", signUpResponse.getRole());
        Assertions.assertNotNull(signUpResponse.getToken());
        Assertions.assertEquals("Bearer", signUpResponse.getType());

        // Validate token
        UserDetails userDetails = userDetailsService.loadUserByUsername(testUser);
        Assertions.assertTrue(authUtils.isTokenValid(signUpResponse.getToken(), userDetails));

        // Attempt login with the newly created user
        LoginResponse loginResponse = authService.login(new LoginRequest(testUser, "password123"));
        Assertions.assertNotNull(loginResponse);
        Assertions.assertEquals(testUser, loginResponse.getUsername());
        Assertions.assertEquals("PATIENT", loginResponse.getRole());
        Assertions.assertTrue(authUtils.isTokenValid(loginResponse.getToken(), userDetails));
    }

    @Test
    public void testSignUpDefaultRole() {
        String testUser = "defaultrole_" + System.currentTimeMillis();
        SignUpRequest request = SignUpRequest.builder()
                .username(testUser)
                .password("password123")
                .build();

        SignUpResponse response = authService.signup(request);
        Assertions.assertEquals("PATIENT", response.getRole());
    }

    @Test
    public void testDuplicateUsernameThrowsConflict() {
        String testUser = "duplicate_" + System.currentTimeMillis();
        SignUpRequest request = SignUpRequest.builder()
                .username(testUser)
                .password("password123")
                .build();

        authService.signup(request);

        Assertions.assertThrows(ResponseStatusException.class, () -> {
            authService.signup(request);
        });
    }

    @Test
    public void testEmptyCredentialsThrowsBadRequest() {
        SignUpRequest emptyUser = SignUpRequest.builder()
                .username("")
                .password("password123")
                .build();

        Assertions.assertThrows(ResponseStatusException.class, () -> {
            authService.signup(emptyUser);
        });

        SignUpRequest emptyPassword = SignUpRequest.builder()
                .username("validUser")
                .password("")
                .build();

        Assertions.assertThrows(ResponseStatusException.class, () -> {
            authService.signup(emptyPassword);
        });
    }
}

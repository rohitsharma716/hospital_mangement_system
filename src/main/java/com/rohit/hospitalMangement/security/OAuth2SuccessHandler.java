package com.rohit.hospitalMangement.security;

import tools.jackson.databind.ObjectMapper;
import com.rohit.hospitalMangement.DTO.LoginResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    // @Lazy breaks the circular dependency:
    //   AuthService → AuthenticationManager (WebSecurityConfig bean)
    //   WebSecurityConfig → OAuth2SuccessHandler → AuthService  ← cycle
    @Lazy
    @Autowired
    private AuthService authService;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        try {
            OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
            OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
            String registrationId = token.getAuthorizedClientRegistrationId();

            log.info("OAuth2 login success for provider: {}, attributes: {}", registrationId, oAuth2User.getAttributes().keySet());

            LoginResponse loginResponse = authService.handleOAuthLoginRequest(oAuth2User, registrationId);

            // Use writeValueAsString to avoid Jackson 3.x Writer API differences
            String json = objectMapper.writeValueAsString(loginResponse);

            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write(json);
            response.getWriter().flush();

        } catch (Exception ex) {
            log.error("OAuth2 success handler error: {}", ex.getMessage(), ex);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write(
                "{\"error\": \"OAuth2 login processing failed\", \"message\": \"" + ex.getMessage() + "\"}"
            );
            response.getWriter().flush();
        }
    }
}

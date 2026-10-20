package com.rohit.hospitalMangement.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    // TODO: Inject required services or repositories in the future (e.g., AdminService)

    // Boilerplate status check endpoint
    @GetMapping("/status")
    public ResponseEntity<String> getAdminStatus() {
        return ResponseEntity.ok("Admin controller is operational");
    }

    // TODO: Implement future admin endpoints (e.g., user management, configuration, audit logs, reports)
}

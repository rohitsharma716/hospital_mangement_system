package com.rohit.hospitalMangement.controller;

import com.rohit.hospitalMangement.Entity.Appointment;
import com.rohit.hospitalMangement.repository.AppointmentRepository;
import com.rohit.hospitalMangement.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping({"/appointments", "/admit"})
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentRepository appointmentRepository;
    private final AppointmentService appointmentService;

    // 1. Get all appointments / admissions
    @GetMapping
    public ResponseEntity<List<Appointment>> getAllAppointments() {
        return ResponseEntity.ok(appointmentRepository.findAll());
    }

    // 2. Get appointment / admission by ID
    @GetMapping("/{id}")
    public ResponseEntity<Appointment> getAppointmentById(@PathVariable Long id) {
        return appointmentRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 3. Book appointment / Admit patient with assigned doctor
    @PostMapping
    public ResponseEntity<Appointment> createAppointment(
            @RequestBody Appointment appointment,
            @RequestParam Long doctorId,
            @RequestParam Long patientId) {
        Appointment created = appointmentService.createNewAppointment(appointment, doctorId, patientId);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // 4. Reassign doctor to appointment / admission
    @PutMapping("/{id}/reassign-doctor/{doctorId}")
    public ResponseEntity<Appointment> reAssignDoctor(
            @PathVariable Long id,
            @PathVariable Long doctorId) {
        Appointment updated = appointmentService.reAssignDoctor(id, doctorId);
        return ResponseEntity.ok(updated);
    }

    // 5. Cancel / Discharge appointment
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelAppointment(@PathVariable Long id) {
        if (!appointmentRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        appointmentRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

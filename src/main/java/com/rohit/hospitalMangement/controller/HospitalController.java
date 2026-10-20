package com.rohit.hospitalMangement.controller;

import com.rohit.hospitalMangement.Entity.Department;
import com.rohit.hospitalMangement.Entity.Doctor;
import com.rohit.hospitalMangement.repository.AppointmentRepository;
import com.rohit.hospitalMangement.repository.DepartmentRepository;
import com.rohit.hospitalMangement.repository.DoctorRepository;
import com.rohit.hospitalMangement.repository.PatientRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping({"/hospital", "/departments"})
@RequiredArgsConstructor
public class HospitalController {

    private final DepartmentRepository departmentRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;

    // 1. Get hospital overview stats
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getHospitalStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalPatients", patientRepository.count());
        stats.put("totalDoctors", doctorRepository.count());
        stats.put("totalDepartments", departmentRepository.count());
        stats.put("totalAppointments", appointmentRepository.count());
        return ResponseEntity.ok(stats);
    }

    // 2. Get all hospital departments
    @GetMapping
    public ResponseEntity<List<Department>> getAllDepartments() {
        return ResponseEntity.ok(departmentRepository.findAll());
    }

    // 3. Get department by ID
    @GetMapping("/{id}")
    public ResponseEntity<Department> getDepartmentById(@PathVariable Long id) {
        return departmentRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 4. Create new department
    @PostMapping
    public ResponseEntity<Department> createDepartment(@RequestBody Department department) {
        Department savedDepartment = departmentRepository.save(department);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedDepartment);
    }

    // 5. Update department
    @PutMapping("/{id}")
    public ResponseEntity<Department> updateDepartment(@PathVariable Long id, @RequestBody Department updatedDepartment) {
        return departmentRepository.findById(id).map(department -> {
            department.setName(updatedDepartment.getName());
            return ResponseEntity.ok(departmentRepository.save(department));
        }).orElse(ResponseEntity.notFound().build());
    }

    // 6. Assign a doctor to a department
    @PostMapping("/{departmentId}/doctors/{doctorId}")
    @Transactional
    public ResponseEntity<Department> addDoctorToDepartment(
            @PathVariable Long departmentId,
            @PathVariable Long doctorId) {
        Department department = departmentRepository.findById(departmentId).orElse(null);
        Doctor doctor = doctorRepository.findById(doctorId).orElse(null);

        if (department == null || doctor == null) {
            return ResponseEntity.notFound().build();
        }

        department.getDoctors().add(doctor);
        doctor.getDepartments().add(department);
        return ResponseEntity.ok(departmentRepository.save(department));
    }

    // 7. Assign department head doctor
    @PutMapping("/{departmentId}/head-doctor/{doctorId}")
    @Transactional
    public ResponseEntity<Department> setHeadDoctor(
            @PathVariable Long departmentId,
            @PathVariable Long doctorId) {
        Department department = departmentRepository.findById(departmentId).orElse(null);
        Doctor doctor = doctorRepository.findById(doctorId).orElse(null);

        if (department == null || doctor == null) {
            return ResponseEntity.notFound().build();
        }

        department.setHeadDoctor(doctor);
        return ResponseEntity.ok(departmentRepository.save(department));
    }

    // 8. Delete department
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDepartment(@PathVariable Long id) {
        if (!departmentRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        departmentRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

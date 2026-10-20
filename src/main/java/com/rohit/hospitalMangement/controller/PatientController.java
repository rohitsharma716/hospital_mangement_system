package com.rohit.hospitalMangement.controller;

import com.rohit.hospitalMangement.DTO.PatientNameBloodGroupResponse;
import com.rohit.hospitalMangement.Entity.Insurance;
import com.rohit.hospitalMangement.Entity.Patient;
import com.rohit.hospitalMangement.repository.PatientRepository;
import com.rohit.hospitalMangement.service.InsuranceService;
import com.rohit.hospitalMangement.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientRepository patientRepository;
    private final PatientService patientService;
    private final InsuranceService insuranceService;

    // 1. Get all patients
    @GetMapping
    public ResponseEntity<List<Patient>> getAllPatients() {
        return ResponseEntity.ok(patientRepository.findAll());
    }

    // 2. Get patient by ID
    @GetMapping("/{id}")
    public ResponseEntity<Patient> getPatientById(@PathVariable Long id) {
        return patientRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 3. Register / Create new patient
    @PostMapping
    public ResponseEntity<Patient> createPatient(@RequestBody Patient patient) {
        Patient savedPatient = patientRepository.save(patient);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPatient);
    }

    // 4. Update patient details
    @PutMapping("/{id}")
    public ResponseEntity<Patient> updatePatient(@PathVariable Long id, @RequestBody Patient updatedPatient) {
        return patientRepository.findById(id).map(patient -> {
            patient.setName(updatedPatient.getName());
            patient.setBirthDate(updatedPatient.getBirthDate());
            patient.setGender(updatedPatient.getGender());
            patient.setBloodGroup(updatedPatient.getBloodGroup());
            patient.setGmail(updatedPatient.getGmail());
            return ResponseEntity.ok(patientRepository.save(patient));
        }).orElse(ResponseEntity.notFound().build());
    }

    // 5. Delete patient
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable Long id) {
        if (!patientRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        patientRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // 6. Get patient blood group & name DTO
    @GetMapping("/{id}/blood-group")
    public ResponseEntity<PatientNameBloodGroupResponse> getPatientBloodGroup(@PathVariable Long id) {
        return ResponseEntity.ok(patientService.getPatientNameAndBloodGroup(id));
    }

    // 7. Get all patients' blood groups & names
    @GetMapping("/blood-groups")
    public ResponseEntity<List<PatientNameBloodGroupResponse>> getAllPatientsBloodGroup() {
        return ResponseEntity.ok(patientService.getAllPatientsNameAndBloodGroup());
    }

    // 8. Assign insurance to patient
    @PostMapping("/{id}/insurance")
    public ResponseEntity<Patient> assignInsurance(@PathVariable Long id, @RequestBody Insurance insurance) {
        return ResponseEntity.ok(insuranceService.assignInsuranceToPatient(insurance, id));
    }

    // 9. Remove insurance from patient
    @DeleteMapping("/{id}/insurance")
    public ResponseEntity<Patient> removeInsurance(@PathVariable Long id) {
        return ResponseEntity.ok(insuranceService.removeInsuranceFromPatient(id));
    }
}

package com.rohit.hospitalMangement.service;

import com.rohit.hospitalMangement.Entity.Patient;
import com.rohit.hospitalMangement.repository.PatientRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.rohit.hospitalMangement.DTO.PatientNameBloodGroupResponse;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;

    @Transactional
    public Patient updatePatientName(Long id) {
        // Patient patient = patientRepository.findById(id)
        // .orElseThrow(() -> new EntityNotFoundException("Patient not found: " + id));

        // patient.setName("rohit sharma");
        // System.out.println("Updated patient: " + patient);
        Patient patient = patientRepository.findByName("Rohan Kumar");

        System.out.println("Patient found by name: " + patient);
        return patient;
    }

    public Patient getPatientById(Long id){
         // search in db first .
         Patient patient = patientRepository.findById(id).orElse(null);
         if (patient != null) {
             System.out.println("get patient using user name : " + patient.getName());
         }
         return patient;
    }

    // Get name and blood group by patient ID
    public PatientNameBloodGroupResponse getPatientNameAndBloodGroup(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Patient not found with id: " + id));

        return new PatientNameBloodGroupResponse(patient.getName(), patient.getBloodGroup());
    }

    // Get name and blood group for all patients
    public List<PatientNameBloodGroupResponse> getAllPatientsNameAndBloodGroup() {
        return patientRepository.findAll()
                .stream()
                .map(patient -> new PatientNameBloodGroupResponse(patient.getName(), patient.getBloodGroup()))
                .toList();
    }

}
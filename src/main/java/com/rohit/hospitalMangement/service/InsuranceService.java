package com.rohit.hospitalMangement.service;


import com.rohit.hospitalMangement.Entity.Insurance;
import com.rohit.hospitalMangement.Entity.Patient;
import com.rohit.hospitalMangement.repository.InsuranceRepository;
import com.rohit.hospitalMangement.repository.PatientRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InsuranceService {

    private final InsuranceRepository insuranceRepository;
    private final PatientRepository patientRepository;

    @Transactional
    public Patient assignInsuranceToPatient(Insurance insurance, Long patientId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new EntityNotFoundException("Patient is not found with ID: " + patientId));

        Insurance savedInsurance = (insurance.getId() == null)
                ? insuranceRepository.save(insurance)
                : insurance;

        patient.setInsurance(savedInsurance);
        return patient;
    }

    @Transactional
    public Patient removeInsuranceFromPatient(Long patientId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new EntityNotFoundException("Patient is not found with ID: " + patientId));

        patient.setInsurance(null);
        return patient;
    }

    @Transactional
    public Patient removeAndDeleteInsurance(Long patientId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new EntityNotFoundException("Patient is not found with ID: " + patientId));

        Insurance insurance = patient.getInsurance();
        if (insurance != null) {
            patient.setInsurance(null);
            insuranceRepository.delete(insurance);
        }

        return patient;
    }
}

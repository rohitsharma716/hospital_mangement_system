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

    private InsuranceRepository insuranceRepository;
    private PatientRepository patientRepository;

    @Transactional
    public Patient assignInsuranceToPatient(Insurance insurance, Long patientId){
        Patient patient = PatientRepository.findById(patientId)
                .orElseThrow( () -> new EntityNotFoundException("Patient is not found with ID + " +  patientId))
        patient.setInsurance(insurance);

//        insurance.setPatient(patient);

        return patient;
    }

}

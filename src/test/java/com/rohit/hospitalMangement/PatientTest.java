package com.rohit.hospitalMangement;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import com.rohit.hospitalMangement.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import com.rohit.hospitalMangement.Entity.Patient;
import java.util.List;
import com.rohit.hospitalMangement.service.PatientService;

@SpringBootTest
public class PatientTest {

    // @Autowired
    // private PatientRepository patientRepository;

    // @Test
    // public void testPatientRepository() {

    // List<Patient> patients = patientRepository.findAll();
    // System.out.println("Patients: " + patients);
    // // Test cases for the PatientRepository can be added here
    // }
    // @Autowired
    // private PatientService patientService;

    // @Test
    // public void testGetPatientById() {
    // Long patientId = 1L; // Replace with an existing patient ID in your database
    // Patient patient = patientRepository.findById(patientId).orElse(null);
    // System.out.println("Patient with ID " + patientId + ": " + patient);
    // // Test cases for getting a patient by ID can be added here
    // }

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private PatientService patientService;

    @Test
    public void testGetPatientDetails() {
        Patient patient = patientRepository.findById(1L).orElse(null);

        if (patient != null) {
            System.out.println(" this is test log: " + patient.getName());
        } else {
            System.out.println(" this is test log: Patient not found with id 1");
        }
    }

    // @Test
    // public void testGetPatientNameAndBloodGroup() {
    // com.rohit.hospitalMangement.DTO.PatientNameBloodGroupResponse response =
    // patientService.getPatientNameAndBloodGroup(1L);
    // System.out.println("DTO Test - Patient Name: " + response.getName());
    // System.out.println("DTO Test - Blood Group: " + response.getBloodGroup());
    // }

    @Test
    public void testGetPatientNameAndBloodGroup() {
        com.rohit.hospitalMangement.DTO.PatientNameBloodGroupResponse response = patientService
                .getPatientNameAndBloodGroup(1L);

        System.out.println(" this is test log : " + response.getName());
        System.out.println(" this is test log : " + response.getBloodGroup());
    }

    @Test
    public void getAllPatientsNameAndBloodGroup(){
         List<com.rohit.hospitalMangement.DTO.PatientNameBloodGroupResponse> response = patientService.getAllPatientsNameAndBloodGroup();


        response.forEach(patient ->
                System.out.println("Name: " + patient.getName()
                        + " Blood Group: " + patient.getBloodGroup())
        );

    }


}

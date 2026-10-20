package com.rohit.hospitalMangement;

import com.rohit.hospitalMangement.Entity.Patient;
import com.rohit.hospitalMangement.service.InsuranceService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class InsuranceTest {

    @Autowired
    private InsuranceService insuranceService;

    @Test
    public void testRemoveInsuranceFromPatient() {
        Patient patient = insuranceService.removeInsuranceFromPatient(1L);

        Assertions.assertNotNull(patient);
        Assertions.assertNull(patient.getInsurance());
        System.out.println("Insurance successfully removed from patient: " + patient.getName());
    }
}

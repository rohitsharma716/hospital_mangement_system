package com.rohit.hospitalMangement;

import com.rohit.hospitalMangement.Entity.Appointment;
import com.rohit.hospitalMangement.service.AppointmentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;

@SpringBootTest
public class AppointmentTest {

     @Autowired
     private AppointmentService appointmentService;

     @Test
     public void createNewAppointment() {

          Appointment appointment = Appointment.builder()
                    .appoimentTime(LocalDateTime.of(2026, 10, 20, 14, 10, 20))
                    .reason("fever").build();

          var newAppointment = appointmentService.createNewAppointment(appointment, 1L, 1L);

          System.out.println(newAppointment);

          var reAssignAppointment = appointmentService.reAssignDoctor(newAppointment.getId(), 2L);

          System.out.println(reAssignAppointment);

          org.junit.jupiter.api.Assertions.assertNotNull(newAppointment);
          org.junit.jupiter.api.Assertions.assertNotNull(newAppointment.getId());
          org.junit.jupiter.api.Assertions.assertEquals(1L, newAppointment.getPatient().getId());
          org.junit.jupiter.api.Assertions.assertEquals("fever", newAppointment.getReason());
          org.junit.jupiter.api.Assertions.assertEquals(2L, reAssignAppointment.getDoctor().getId());
     }

}

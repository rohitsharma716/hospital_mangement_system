package com.rohit.hospitalMangement.service;

import com.rohit.hospitalMangement.Entity.Appointment;
import com.rohit.hospitalMangement.Entity.Doctor;
import com.rohit.hospitalMangement.Entity.Patient;
import com.rohit.hospitalMangement.repository.AppointmentRepository;
import com.rohit.hospitalMangement.repository.DoctorRepository;
import com.rohit.hospitalMangement.repository.InsuranceRepository;
import com.rohit.hospitalMangement.repository.PatientRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;

    @Transactional
    public Appointment createNewAppointment(Appointment appointment, Long doctorId, Long patientId) {

        Doctor doctor = doctorRepository.findById(doctorId).orElseThrow();
        Patient patient = patientRepository.findById(patientId).orElseThrow();

        if (appointment.getId() != null) {
            throw new IllegalArgumentException("Appointment should not already have an ID");
        }

        appointment.setPatient(patient);
        appointment.setDoctor(doctor);

        patient.getAppointment().add(appointment);

        return appointmentRepository.save(appointment);
    }

    @Transactional
    public Appointment reAssignDoctor(Long appointmentId, Long doctorId) {

        Appointment appointment = appointmentRepository.findById(appointmentId).orElseThrow();
        Doctor doctor = doctorRepository.findById(doctorId).orElseThrow();

        Doctor oldDoctor = appointment.getDoctor();
        if (oldDoctor != null) {
            oldDoctor.getAppointments().remove(appointment);
        }

        appointment.setDoctor(doctor);
        doctor.getAppointments().add(appointment);

        return appointment;
    }

}

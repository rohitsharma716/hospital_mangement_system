package com.rohit.hospitalMangement.repository;

import com.rohit.hospitalMangement.Entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
}
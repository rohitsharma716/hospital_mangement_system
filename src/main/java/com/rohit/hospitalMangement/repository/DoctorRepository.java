package com.rohit.hospitalMangement.repository;

import com.rohit.hospitalMangement.Entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
}

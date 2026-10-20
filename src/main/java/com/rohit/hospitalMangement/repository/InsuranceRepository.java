package com.rohit.hospitalMangement.repository;

import com.rohit.hospitalMangement.Entity.Insurance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InsuranceRepository extends JpaRepository<Insurance, Long> {
}
package com.rohit.hospitalMangement.repository;

import com.rohit.hospitalMangement.Entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
}
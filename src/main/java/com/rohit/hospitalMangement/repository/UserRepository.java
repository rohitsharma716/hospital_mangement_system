package com.rohit.hospitalMangement.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rohit.hospitalMangement.Entity.Users;

@Repository
public interface UserRepository extends JpaRepository<Users,Long> {
    
    Optional<Users> findByUsername(String username);

    boolean existsByUsername(String username);


}
    

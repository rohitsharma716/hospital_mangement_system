package com.rohit.hospitalMangement.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Doctor {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(nullable = false, length = 50)
        private String name;

        @Column(length = 100)
        private String specilization;

        @Column(nullable = true, length = 100, unique = true)
        private String email;

        @ManyToMany(mappedBy = "doctors")
        private Set<Department> departments = new HashSet<>();

        @OneToMany(mappedBy = "doctor")
        private Set<Appointment> appointments = new HashSet<>();

}

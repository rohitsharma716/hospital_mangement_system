package com.rohit.hospitalMangement.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100, unique = true)
    private String name;

    @OneToOne
    private Doctor headDoctor;

    @ManyToMany
    @JoinTable(name = "my_dpt_doctors", joinColumns = @JoinColumn(name = "dpt_id"), inverseJoinColumns = @JoinColumn(name = "doctor_id"))
    private Set<Doctor> doctors = new HashSet<>();

}

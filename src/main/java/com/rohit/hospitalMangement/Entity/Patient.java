package com.rohit.hospitalMangement.Entity;

import com.rohit.hospitalMangement.Entity.type.BloodGroup;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Entity 
@ToString 
@Getter 
@Setter 
public class Patient{

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String birthDate;

    @ToString.Exclude // Exclude the gender field from the generated toString() method
    private String gender;

    @Enumerated(EnumType.STRING)
    private BloodGroup bloodGroup;

    private String gmail;

    @OneToOne
    private Insurance insurance;

    @OneToMany(mappedBy = "patient")
    private List<Appointment> appointment;


       
}
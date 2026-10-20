package com.rohit.hospitalMangement.Entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Insurance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,unique = true,length = 50)
    private String policyNumber;

    @Column(nullable = false,unique = true)
    private String provider;

    @Column(nullable = false)
    private LocalDate valideTill;

    @CreationTimestamp
    @Column(nullable = false,updatable = false)
    private LocalDateTime createAt;

//    @OnetoOne(mappedBy= "insurance") // inverse side
//    private Patient patient;


}

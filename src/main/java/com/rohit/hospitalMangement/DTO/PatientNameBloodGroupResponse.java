package com.rohit.hospitalMangement.DTO;

import com.rohit.hospitalMangement.Entity.type.BloodGroup;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientNameBloodGroupResponse {
    private String name;
    private BloodGroup bloodGroup;
}

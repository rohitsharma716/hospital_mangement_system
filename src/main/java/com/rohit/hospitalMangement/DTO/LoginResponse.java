package com.rohit.hospitalMangement.DTO;


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.Data;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class LoginResponse {

     private String token;
     private String type;
     private String username;
     private String role;


}

package com.rohit.hospitalMangement.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SignUpResponse {

    private Long id;
    private String username;
    private String role;
    private String token;
    private String type;
    private String message;

}

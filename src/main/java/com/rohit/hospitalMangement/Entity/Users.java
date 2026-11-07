package com.rohit.hospitalMangement.Entity;


import com.rohit.hospitalMangement.Entity.type.ProviderType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;   
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name="users")
@Builder
public class Users implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true,nullable=false)
    private String username;

    @Column(nullable=true)
    private String password;


    @Column(nullable = false)
    private String role;

    @Column(name = "provider_id")
    private String providerId;

    @Enumerated(EnumType.STRING)
    @Column(name = "provider_type")
    private ProviderType providerType;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
    if (role == null) return List.of();
    String formattedRole = role.startsWith("ROLE_") ? role : "ROLE_" + role;
    return List.of(new org.springframework.security.core.authority.SimpleGrantedAuthority(formattedRole));
}
}

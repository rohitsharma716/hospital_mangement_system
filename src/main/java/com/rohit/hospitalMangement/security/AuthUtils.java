package com.rohit.hospitalMangement.security;

import org.springframework.stereotype.Component;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;




@Component
public class AuthUtils {
    
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    private SecretKey getSigningKey(){
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUsername(String token){
           return extractClaim(token,Claims::getSubject);
    }
    
    public <T> T extractClaim(String token ,Function<Claims,T> claimsResolver){
        final Claims claims = extractAllClaim(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(UserDetails userdetails,String role){
           
        Map<String, Object > claims = new HashMap<>();
        claims.put("role", role);
        return generateToken(claims, userdetails);
    }

    public String generateToken(Map<String, Object> claims, UserDetails userDetails) {
    return Jwts.builder()
            .claims(claims)
            .subject(userDetails.getUsername())
            .issuedAt(new Date(System.currentTimeMillis()))
            .expiration(new Date(System.currentTimeMillis() + jwtExpiration))
            .signWith(getSigningKey())
            .compact();
}
     public boolean isTokenValid(String token, UserDetails userdetails){
          final String username =  extractUsername(token);
          return (username.equals(userdetails.getUsername())) && !isTokenExpired(token);
          
     }

     private boolean isTokenExpired(String token){
          return extractClaim(token,Claims::getExpiration)
          .before(new Date());
     }

     private Claims extractAllClaim(String token){
          return Jwts.parser()
    .verifyWith(getSigningKey())
    .build()
    .parseSignedClaims(token)
    .getPayload();
     }
        

}

package com.rohit.hospitalMangement.security;

import com.rohit.hospitalMangement.Entity.type.ProviderType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.core.user.OAuth2User;
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
@Slf4j
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

     public ProviderType getProviderTypeFromProviderId(String registrationId) {
         ProviderType providerType = switch (registrationId.toLowerCase()) {
             case "google" -> ProviderType.GOOGLE;
             case "github" -> ProviderType.GITHUB;
             case "facebook" -> ProviderType.FACEBOOK;
             default -> throw new IllegalArgumentException("unsupported OAuth2 provider: " + registrationId);
         };
         return providerType;

     }

     public String determineProviderIdFromOAuthUser(OAuth2User oAuth2User, String registrationId){
           String providerId = switch(registrationId.toLowerCase()){
                 case "google" -> oAuth2User.getAttribute("sub");
                 // GitHub returns "id" as an Integer.
                 // Cast to Object explicitly to force String.valueOf(Object) overload —
                 // without it the compiler picks String.valueOf(char[]) causing ClassCastException.
                 case "github" -> String.valueOf((Object) oAuth2User.getAttribute("id"));
                 default ->  {
                     log.error("unable to detemine the providerId : " +  registrationId);
                     throw new IllegalArgumentException("unable to determine the providerid from registrationId");
                 }

           };
           if(providerId == null || providerId.isBlank() || providerId.equals("null")){
               log.error("unable to detemine the provider for provider id " +  registrationId);
               throw new IllegalArgumentException("unable to determine the provider id from registeer id " + registrationId);


           }
           return providerId;
     }
     public String determineUserNameFromOAuth2User(OAuth2User oAuth2User, String registrationId , String providerId){

        String email = oAuth2User.getAttribute("email");
        if(email != null && !email.isBlank()){
              return email;
        }
        return switch (registrationId.toLowerCase()){
             case "google" -> oAuth2User.getAttribute("sub");
             case "github" -> oAuth2User.getAttribute("login");
             default ->  providerId;
        };
     }


        

}

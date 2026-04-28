package com.kraken.cube.common.util;


import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.lifetime}")
    private int lifetime_ms;
    

    private SecretKey getKey(){
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateToken(Authentication authenfication){
        UserDetails userDetails = (UserDetails)authenfication.getPrincipal();

        return  Jwts.builder().subject(userDetails.getUsername())
                              .issuedAt(new Date())
                              .expiration(new Date(new Date().getTime() + lifetime_ms))
                              .signWith(getKey())
                              .compact();
    }


    public String getNameFromJwt(String token){
        return Jwts.parser()
                   .verifyWith(getKey())
                   .build()
                   .parseSignedClaims(token)
                   .getPayload()
                   .getSubject();
    }

    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                   .verifyWith(getKey())
                   .build()
                   .parseSignedClaims(token)
                   .getPayload();
}
}

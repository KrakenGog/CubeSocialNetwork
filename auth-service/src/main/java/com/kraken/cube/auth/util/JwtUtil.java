package com.kraken.cube.auth.util;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.kraken.cube.auth.entity.Role;
import com.kraken.cube.auth.entity.UserAuth;

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

    public String generateToken(UserAuth userAuth){
        List<String> roles = userAuth.getRoles().stream()
                                        .map(Role::getName)
                                        .collect(Collectors.toList());


        return  Jwts.builder().subject(userAuth.getUsername())
                              .issuedAt(new Date())
                              .claim("roles", roles)
                              .claim("id", userAuth.getId())
                              .expiration(new Date(new Date().getTime() + lifetime_ms))
                              .signWith(getKey())
                              .compact();
    }
}

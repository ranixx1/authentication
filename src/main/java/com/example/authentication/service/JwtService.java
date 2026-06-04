package com.example.authentication.service;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;

import com.example.authentication.model.User;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
    private static final String SECRET_KEY = "3cfa76ef14937c1c0ea519f8a5c3519f1234567890abcdef1234567890abcdef";

    public String generateToken( User user){
        return Jwts.builder()
            .subject(user.getUsername())
            .claim("email", user.getEmail())
            .claim("role", user.getType().name())
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + 8640000))
            .signWith(getSigningKey(), Jwts.SIG.HS256)
            .compact();
    }

    private SecretKey getSigningKey(){
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}

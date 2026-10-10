package com.ecommerce.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.List;

@Service
public class JwtService {

    private final SecretKey secretKey;

    public JwtService(@Value("${jwt.secret}") String secretKey) {
        byte[] keys = Decoders.BASE64.decode(secretKey);
        this.secretKey = Keys.hmacShaKeyFor(keys);
    }

    public boolean isTokenValid(String token) {
        try {
            getClaims(token);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public String extractUserEmail(String token) {
        Claims claims = getClaims(token);
        return claims.getSubject();
    }

    public Long extractUserId(String token) {
        Claims claims = getClaims(token);
        return claims.get("userId",Long.class);
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public List<String> extractRoles(String token) {
        Claims claims = getClaims(token);
        return claims.get("roles",List.class);
    }

}

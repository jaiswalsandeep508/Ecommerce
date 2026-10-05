package com.ecommerce.security;

import com.ecommerce.exception.InvalidJwtTokenException;
import com.ecommerce.exception.JwtTokenExpiredException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;

@Service
public class JwtService {

    private final SecretKey secretKey;

    public JwtService(@Value("${jwt.secret}") String secretKey) {
        byte[] keys = Decoders.BASE64.decode(secretKey);
        this.secretKey = Keys.hmacShaKeyFor(keys);
    }

    public boolean isTokenValid(String token) {
        getClaims(token);
        return true;
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
        try {
            return Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException ex) {
            throw new JwtTokenExpiredException("JWT token has expired");
        } catch (JwtException ex) {
            throw new InvalidJwtTokenException("Invalid JWT token");
        }
    }

    public String generateToken(
            Long userId,
            String userEmail,
            List<String> roles) {
        return Jwts.builder()
                .subject(userEmail)
                .claim("userId", userId)
                .claim("roles", roles)
                .issuedAt(new Date())
                .expiration(new Date(
                        System.currentTimeMillis() + 120000
                ))
                .signWith(secretKey)
                .compact();
    }

    public List<String> extractRoles(String token) {
        Claims claims = getClaims(token);
        return claims.get("roles",List.class);
    }

}

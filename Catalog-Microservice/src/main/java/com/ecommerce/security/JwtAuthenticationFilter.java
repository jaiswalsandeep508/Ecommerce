package com.ecommerce.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");
        if(authorizationHeader==null ||
            !authorizationHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request,response);
            return ;
        }
        String token = authorizationHeader.substring(7);
        try {
            if(jwtService.isTokenValid(token) &&
                SecurityContextHolder.getContext().getAuthentication()==null) {
                String userEmail = jwtService.extractUserEmail(token);
                Long userId = jwtService.extractUserId(token);
                List<String> roles = jwtService.extractRoles(token);
                List<SimpleGrantedAuthority> authorities =
                        roles.stream()
                                .map(role ->
                                        new SimpleGrantedAuthority("ROLE_"+role))
                                .toList();
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userEmail,
                                null,
                                authorities
                        );
                authentication.setDetails(userId);
                SecurityContextHolder.getContext()
                        .setAuthentication(authentication);
            }
        } catch (Exception ex) {
            SecurityContextHolder.clearContext();
        }
        filterChain.doFilter(request,response);
    }
}

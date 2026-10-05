package com.ecommerce.config;

import com.ecommerce.security.JwtAuthenticationFilter;
import com.ecommerce.security.SecurityAccessDeniedException;
import com.ecommerce.security.SecurityAuthEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    private final SecurityAccessDeniedException securityAccessDeniedException;
    private final SecurityAuthEntryPoint securityAuthEntryPoint;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) {
        httpSecurity
                .csrf(csrf -> csrf.disable())
                .headers(headers ->
                        headers.frameOptions(frameOption -> frameOption.disable())
                )
                .sessionManagement(session
                        -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers(
                                        "/api/v1/users/register",
                                        "/api/v1/auth/**",
                                        "/h2-console/**"
                                ).permitAll()

                                .requestMatchers("/api/v1/roles/**")
                                .hasRole("ADMIN")

                                .requestMatchers("/api/v1/users/**")
                                .hasAnyRole("ADMIN", "CUSTOMER")

                                .requestMatchers("/api/v1/addresses/**")
                                .hasAnyRole("ADMIN", "CUSTOMER")

                                .anyRequest()
                                .authenticated()
                )
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(securityAuthEntryPoint)
                        .accessDeniedHandler(securityAccessDeniedException)
                )
                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );
        return httpSecurity.build();
    }
}

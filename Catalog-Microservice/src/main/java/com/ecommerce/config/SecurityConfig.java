package com.ecommerce.config;

import com.ecommerce.security.JwtAuthenticationFilter;
import com.ecommerce.security.SecurityAccessDeniedException;
import com.ecommerce.security.SecurityAuthEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final SecurityAuthEntryPoint securityAuthEntryPoint;
    private final SecurityAccessDeniedException securityAccessDeniedException;

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
                        auth
                                .requestMatchers(
                                        "/h2-console/**"
                                ).permitAll()

                                .requestMatchers(
                                        HttpMethod.GET,
                                        "/api/v1/products/**"
                                ).permitAll()

                                .requestMatchers(
                                        HttpMethod.POST,
                                        "/api/v1/products/filter"
                                ).permitAll()

                                .requestMatchers(
                                        HttpMethod.GET,
                                        "/api/v1/categories/**"
                                ).permitAll()

                                .requestMatchers(
                                        HttpMethod.GET,
                                        "/api/v1/brands/**"
                                ).permitAll()

                                .requestMatchers(
                                        HttpMethod.GET,
                                        "/api/v1/reviews/**"
                                ).permitAll()

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

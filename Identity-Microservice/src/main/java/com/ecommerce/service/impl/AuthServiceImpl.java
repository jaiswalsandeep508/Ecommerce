package com.ecommerce.service.impl;

import com.ecommerce.dto.request.LoginRequest;
import com.ecommerce.dto.response.LoginResponse;
import com.ecommerce.exception.BadRequestException;
import com.ecommerce.model.User;
import com.ecommerce.security.JwtService;
import com.ecommerce.service.AuthService;
import com.ecommerce.service.factory.UserFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserFactory userFactory;

    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        User user = userFactory.getUserByEmail(loginRequest.getEmail());
        if(!passwordEncoder.matches(
                loginRequest.getPassword(),
                user.getPassword())) {
            throw new BadRequestException("Invalid password");
        }
        List<String> roles = user.getRoles()
                .stream()
                .map(role -> role.getRoleType().name())
                .toList();
        String token = jwtService.generateToken(
                user.getUserId(),
                user.getEmail(),
                roles);
        return new LoginResponse(token);
    }

}

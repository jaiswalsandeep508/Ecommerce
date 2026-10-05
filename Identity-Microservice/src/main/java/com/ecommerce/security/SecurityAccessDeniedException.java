package com.ecommerce.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class SecurityAccessDeniedException implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    public SecurityAccessDeniedException(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");

        Map<String,Object> errorResponse = new HashMap<>();
        errorResponse.put("status",403);
        errorResponse.put("error","Forbidden");
        errorResponse.put(
                "message",
                "You do not have permission to access this resource"
        );

        response.getWriter().write(
                objectMapper.writeValueAsString(errorResponse)
        );
    }
}

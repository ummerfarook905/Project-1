package com.example.aquafin.services;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Service;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class CustomSuccessHandler implements AuthenticationSuccessHandler{

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException{
                                var authorities= authentication.getAuthorities();
                                var roles = authorities.stream()
                                .map(r -> r.getAuthority())
                                .findFirst()
                                .map(r -> r.startsWith("ROLE_") ? r.substring(5) : r)  
                                .orElse("");

                                switch (roles){
                                    case "SUPER_ADMIN" -> response.sendRedirect("/super-admin");
                                    case "ADMIN" -> response.sendRedirect("/admin");
			                        case "USER" -> response.sendRedirect("/dashboard");
			                        default -> response.sendRedirect("/access-denied");
                                }
                                        }

}

package com.bank.config;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.var;

@Component
public class MyAuthSuccessHandler implements AuthenticationSuccessHandler{

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
       
                for(var authority : authentication.getAuthorities()){
                    String role = authority.getAuthority();

                    if (role.equals("ROLE_ADMIN")) {
                        response.sendRedirect("/admins/dashboard");
                        return;
                    }

                    if (role.equals("ROLE_USER")) {
                        response.sendRedirect("/users/dashboard");
                        return;
                    }
                }
                
                response.sendRedirect("/");
    }

}

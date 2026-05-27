package com.example.board.config;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        String requestedWith = request.getHeader("X-Requested-With");
        boolean isAjax = "XMLHttpRequest".equals(requestedWith);

        if (isAjax) {
            response.setContentType("application/json; charset=UTF-8");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("""
            {"message": "로그인이 필요합니다.", "status": 401}
        """);
        } else {
            response.sendRedirect("/member/login");
        }
    }
}
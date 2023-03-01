package com.example.sweater_letscode.spring.recaptcha;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RecaptchaFilter extends OncePerRequestFilter {
    private final RecaptchaAuthenticationFailureHandler failureHandler;

    public RecaptchaFilter(RecaptchaAuthenticationFailureHandler failureHandler) {
        this.failureHandler = failureHandler;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        if ("POST".equalsIgnoreCase(request.getMethod()) && request.getRequestURI().equals("/login")) {
            String recaptchaResponse = request.getParameter("g-recaptcha-response");
            boolean isValid = failureHandler.getRecaptchaService().verify(recaptchaResponse).isSuccess();
            if (!isValid) {
                failureHandler.onAuthenticationFailure(request, response, new BadCredentialsException("Invalid reCAPTCHA"));
                return;
            }
        }
        filterChain.doFilter(request, response);
    }
}

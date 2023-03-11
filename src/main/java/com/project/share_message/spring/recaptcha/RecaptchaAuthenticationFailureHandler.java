package com.project.share_message.spring.recaptcha;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@Component
@Data
public class RecaptchaAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Autowired
    private HttpServletRequest request;

    @Autowired
    private RecaptchaRegisterService recaptchaService;
    private static final Logger log = LoggerFactory.getLogger(RecaptchaAuthenticationFailureHandler.class);

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String username = request.getParameter("username");
        String recaptchaResponse = request.getParameter("g-recaptcha-response");

        RecaptchaResponse verify = recaptchaService.verify(recaptchaResponse);

        if (verify.isSuccess()) {
            log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> g-recaptcha-response: {}", recaptchaResponse);
            request.getSession().setAttribute("username", username);
            response.sendRedirect("/login?error");
        } else {
            response.sendRedirect(request.getContextPath() + "/login?error&recaptcha");
        }
    }
}

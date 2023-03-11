package com.project.share_message.spring.recaptcha;

import org.springframework.stereotype.Service;

@Service
public interface RecaptchaService {
    RecaptchaResponse verify(String response);
}

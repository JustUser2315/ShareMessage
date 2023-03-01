package com.example.sweater_letscode.spring.recaptcha;

import com.example.sweater_letscode.spring.recaptcha.RecaptchaResponse;
import org.springframework.stereotype.Service;

@Service
public interface RecaptchaService {
    RecaptchaResponse verify(String response);
}

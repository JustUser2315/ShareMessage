package com.example.sweater_letscode.spring.recaptcha;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@ToString
@PropertySource("classpath:spring.properties")
//@ConfigurationProperties(prefix = "google.recaptcha.key")
public class RecaptchaKeys {
    @Value("${google.recaptcha.key.site}")
    private String site;
    @Value("${google.recaptcha.key.secret}")
    private String secret;
}

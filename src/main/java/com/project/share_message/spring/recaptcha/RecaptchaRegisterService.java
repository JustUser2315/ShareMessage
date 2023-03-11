package com.project.share_message.spring.recaptcha;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
@Component
@RequiredArgsConstructor
public class RecaptchaRegisterService implements RecaptchaService{
    private final RecaptchaKeys recaptchaKeys;
    private final RestTemplate restTemplate;

    private static final Logger log = LoggerFactory.getLogger(RecaptchaRegisterService.class);

    @Override
    public RecaptchaResponse verify(String response) {
        //API Request
        URI verifyURI = URI.create(String.format("https://www.google.com/recaptcha/api/siteverify?secret=%s&response=%s",
                recaptchaKeys.getSecret(), response));

        // make HTTP call with RestTemplate
        RecaptchaResponse recaptchaResponse = restTemplate.getForObject(verifyURI, RecaptchaResponse.class);

        // log the Returned Recaptcha response object
        log.info(">>>>>>>>>>>>>>>>>> response after verifying: {}", recaptchaResponse);


        return recaptchaResponse;
    }
}

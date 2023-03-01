package com.example.sweater_letscode.spring.recaptcha;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
@Getter
@Setter
@ToString
public class RecaptchaResponse {
    private boolean success;
    @JsonProperty("challenge_ts")
    private String challengeTs;
    private String hostname;
    @JsonProperty("error-codes")
    List<String> errorCodes;

    public List<String> getAllErrors(){
        if(!isSuccess()){
            return RecaptchaErrors.errors.values()
                    .stream().toList();
        }
        return null;
    }

}

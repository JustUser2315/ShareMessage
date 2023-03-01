package com.example.sweater_letscode.spring;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.constraints.Pattern;

@Data
@RequiredArgsConstructor
@Component
public class LoginForm {
    @Pattern(regexp = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,}$",
            message = "Sorry, the email address you entered is invalid. Please enter a valid email address in the format example@example.com.")
    private String email;

    @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$",
            message = "Sorry, the password you entered does not meet the required format. " +
                    "Password must contain at least 8 characters, with at least one uppercase letter and one digit. " +
                    "Please try again with a valid password.\n")
    private String password;

}

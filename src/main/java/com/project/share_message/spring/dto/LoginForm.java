package com.project.share_message.spring.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@Builder

public class LoginForm {
    @Size(min = 3, max = 256, message = "Username must contain at least 3 characters. Or this username is already exists." +
            "Please try again with other username")
    private String username;
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$",
            message = "Sorry, the password you entered does not meet the required format. " +
                    "Password must contain at least 8 characters, with at least one uppercase letter and one digit. " +
                    "Please try again with a valid password.\n")
    private String password;
}

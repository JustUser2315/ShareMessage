package com.example.sweater_letscode.spring.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Pattern;

@Data
@Builder
public class UpdatePasswordEditDto {
     @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$",
    message = "Sorry, the new password you entered does not meet the required format. " +
            "Password must contain at least 8 characters, with at least one uppercase letter and one digit. " +
            "Please try again with a valid password.\n")
   private  String newPassword;
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$",
    message = "Sorry, the confirm password you entered does not meet the required format. " +
            "Password must contain at least 8 characters, with at least one uppercase letter and one digit. " +
            "Please try again with a valid password.\n")
    private String confirmPassword;
     @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$",
    message = "Sorry, the old password you entered does not meet the required format. " +
            "Password must contain at least 8 characters, with at least one uppercase letter and one digit. " +
            "Please try again with a valid password.\n")
   private  String oldPassword;
}

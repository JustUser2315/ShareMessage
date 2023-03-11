package com.project.share_message.spring.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEditDto {
    @Pattern(regexp = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,}$",
    message = "Sorry, the email address you entered is invalid. Please enter a valid email address in the format example@example.com.")
    private String email;
    @Size(min = 3, max = 256, message = "Username must contain at least 3 characters. Or this username is already exists." +
            "Please try again with other username")
    private String username;
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$",
    message = "Sorry, the password you entered does not meet the required format. " +
            "Password must contain at least 8 characters, with at least one uppercase letter and one digit. " +
            "Please try again with a valid password.\n")
    private String password;
    private boolean active;
    private String activationCode;
    private MultipartFile avatar;
    private Set<RoleEditDto> roles;
    }

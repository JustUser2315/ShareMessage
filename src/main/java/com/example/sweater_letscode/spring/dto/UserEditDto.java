package com.example.sweater_letscode.spring.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEditDto {
    @Pattern(regexp = "[0-9]*[A-z]+[0-9]*@[A-z]+\\.[A-z]{2,15}", message = "Username pattern: [test@mail.com]")
    @Size(max = 256)
    private String username;
    @Size(min = 8, message = "Minimum 8 characters in password")
    private String password;
    private boolean active = true;
    private Set<RoleEditDto> roles;
}

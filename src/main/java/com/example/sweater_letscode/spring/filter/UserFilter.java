package com.example.sweater_letscode.spring.filter;

import com.example.sweater_letscode.spring.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserFilter {
    private String username;
    private String email;
    private String active;
    private Set<Role> roles;
}

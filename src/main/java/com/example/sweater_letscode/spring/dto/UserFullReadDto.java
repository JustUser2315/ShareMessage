package com.example.sweater_letscode.spring.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserFullReadDto {
    private Long id;
    private String username;
    private boolean active;
    private Set<RoleReadDto> roles;
}

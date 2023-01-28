package com.example.sweater_letscode.spring.mapper;

import com.example.sweater_letscode.spring.dto.RoleReadDto;
import com.example.sweater_letscode.spring.entity.Role;
import org.springframework.stereotype.Component;

@Component
public class RoleEntityToReadMapper implements MyCustomMapper<Role, RoleReadDto> {
    @Override
    public RoleReadDto map(Role role) {
        return RoleReadDto.builder()
                .name(role.getName())
                .build();
    }
}

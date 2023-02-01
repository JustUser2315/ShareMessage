package com.example.sweater_letscode.spring.mapper;

import com.example.sweater_letscode.spring.dto.RoleReadDto;
import com.example.sweater_letscode.spring.entity.Role;
import org.springframework.stereotype.Component;

@Component
public class RoleReadToEntityMapper implements MyCustomMapper<RoleReadDto, Role>{
    @Override
    public Role map(RoleReadDto roleReadDto) {
        return Role.builder()
                .id(roleReadDto.getId())
                .name(roleReadDto.getName())
                .build();
    }
}

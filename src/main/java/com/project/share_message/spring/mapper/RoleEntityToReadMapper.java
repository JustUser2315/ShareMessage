package com.project.share_message.spring.mapper;

import com.project.share_message.spring.dto.RoleReadDto;
import com.project.share_message.spring.entity.Role;
import org.springframework.stereotype.Component;

@Component
public class RoleEntityToReadMapper implements MyCustomMapper<Role, RoleReadDto> {
    @Override
    public RoleReadDto map(Role role) {
        return RoleReadDto.builder()
                .id(role.getId())
                .name(role.getName())
                .build();
    }
}

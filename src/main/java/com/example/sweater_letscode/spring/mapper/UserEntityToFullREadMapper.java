package com.example.sweater_letscode.spring.mapper;

import com.example.sweater_letscode.spring.dto.RoleReadDto;
import com.example.sweater_letscode.spring.dto.UserFullReadDto;
import com.example.sweater_letscode.spring.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserEntityToFullREadMapper implements MyCustomMapper<User, UserFullReadDto> {
    @Override
    public UserFullReadDto map(User user) {
        RoleEntityToReadMapper roleEntityToReadMapper = new RoleEntityToReadMapper();
        var roles = user.getRoles().stream().map(roleEntityToReadMapper::map).collect(Collectors.toSet());
        return UserFullReadDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .active(user.isActive())
                .roles(roles)
                .build();
    }
}

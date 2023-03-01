package com.example.sweater_letscode.spring.mapper;

import com.example.sweater_letscode.spring.dto.RoleReadDto;
import com.example.sweater_letscode.spring.dto.UserReadDto;
import com.example.sweater_letscode.spring.entity.User;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserEntityToReadMapper implements MyCustomMapper<User, UserReadDto> {
    private UserReadDto mapSubs(User user) {

        RoleEntityToReadMapper roleEntityToReadMapper = new RoleEntityToReadMapper();
        Set<RoleReadDto> roles = user.getRoles().stream().map(roleEntityToReadMapper::map).collect(Collectors.toSet());
        return UserReadDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .active(user.isActive())
                .roles(roles)
                .avatar(user.getAvatar())
                .activationCode(user.getActivationCode())
                .build();
    }
    @Override
    public UserReadDto map(User user) {

        RoleEntityToReadMapper roleEntityToReadMapper = new RoleEntityToReadMapper();
        Set<RoleReadDto> roles = user.getRoles().stream().map(roleEntityToReadMapper::map).collect(Collectors.toSet());
        return UserReadDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .active(user.isActive())
                .roles(roles)
                .avatar(user.getAvatar())
                .activationCode(user.getActivationCode())
                .subscribers(user.getSubscribers().stream().map(this::mapSubs).collect(Collectors.toSet()))
                .subscriptions(user.getSubscriptions().stream().map(this::mapSubs).collect(Collectors.toSet()))
                .build();
    }
}

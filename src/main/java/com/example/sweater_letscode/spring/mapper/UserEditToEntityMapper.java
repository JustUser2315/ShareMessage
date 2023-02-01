package com.example.sweater_letscode.spring.mapper;

import com.example.sweater_letscode.spring.dto.RoleEditDto;
import com.example.sweater_letscode.spring.dto.RoleReadDto;
import com.example.sweater_letscode.spring.dto.UserEditDto;
import com.example.sweater_letscode.spring.entity.Role;
import com.example.sweater_letscode.spring.entity.User;
import com.example.sweater_letscode.spring.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserEditToEntityMapper implements MyCustomMapper<UserEditDto, User> {
    private final RoleEntityToReadMapper roleEntityToReadMapper;
    private final RoleRepository roleRepository;


    public User mapForUpdate(UserEditDto userEditDto, User user) {
        if (userEditDto.getRoles() == null) {
            userEditDto.setRoles(Collections.emptySet());
        }

        var strings = userEditDto.getRoles().stream().map(RoleEditDto::getName).toList();
        var allByNameIn = roleRepository.findAllByNameIn(strings);
        for (Role r : allByNameIn) {
            user.getRoles().add(r);
        }

        if (userEditDto.getUsername() == null) {
            userEditDto.setUsername(user.getUsername());
        }

        return User.builder()
                .id(user.getId())
                .password(user.getPassword())
                .username(userEditDto.getUsername())
                .active(userEditDto.isActive())
                .roles(user.getRoles())
                .build();

    }

    @Override
    public User map(UserEditDto userEditDto) {
        var role_user = roleRepository.findRoleByName("ROLE_USER");
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        var user = User.builder()
                .username(userEditDto.getUsername())
                .password(passwordEncoder.encode(userEditDto.getPassword()))
                .active(userEditDto.isActive())
                .roles(new HashSet<>())
                .build();
        user.setRoles(new HashSet<>());
        if (role_user.isPresent()) {
            user.getRoles().add(role_user.get());
        } else {
            roleRepository.save(Role.builder().name("ROLE_USER").build());
        }
        return user;

    }
}

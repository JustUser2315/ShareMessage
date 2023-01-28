package com.example.sweater_letscode.spring.mapper;

import com.example.sweater_letscode.spring.dto.RoleReadDto;
import com.example.sweater_letscode.spring.dto.UserEditDto;
import com.example.sweater_letscode.spring.entity.Role;
import com.example.sweater_letscode.spring.entity.User;
import com.example.sweater_letscode.spring.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserEditToEntityMapper implements MyCustomMapper<UserEditDto, User> {
    private final RoleEntityToReadMapper roleEntityToReadMapper;
    private final RoleRepository roleRepository;

    @Override
    public User map(UserEditDto userEditDto) {
     PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        var rolesName = userEditDto.getRoles().stream().map(RoleReadDto::getName)
                .toList();

        var maybeRoles = roleRepository.findAllByNameIn(rolesName);
        var user = User.builder()
                .username(userEditDto.getUsername())
                .password(passwordEncoder.encode(userEditDto.getPassword()))
                .active(true)
                .build();
        user.setRoles(new HashSet<>());
       maybeRoles.forEach(user.getRoles()::add);
        return user;

    }
}

package com.example.sweater_letscode.spring.mapper;

import com.example.sweater_letscode.spring.dto.UserReadDto;
import com.example.sweater_letscode.spring.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserEntityToReadMapper implements MyCustomMapper<User, UserReadDto>{
    @Override
    public UserReadDto map(User user) {
        return UserReadDto.builder()
                .username(user.getUsername())
                .build();
    }
}

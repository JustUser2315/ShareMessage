package com.example.sweater_letscode.spring.service;

import com.example.sweater_letscode.spring.TestBaseApplication;
import com.example.sweater_letscode.spring.dto.RoleReadDto;
import com.example.sweater_letscode.spring.dto.UserEditDto;
import com.example.sweater_letscode.spring.dto.UserReadDto;
import com.example.sweater_letscode.spring.entity.User;
import com.example.sweater_letscode.spring.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.TestConstructor;
import org.testcontainers.shaded.org.bouncycastle.crypto.RawAgreement;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@RequiredArgsConstructor
@ExtendWith(MockitoExtension.class)
class UserServiceTest extends TestBaseApplication {
    private final UserService userService;
    private final UserRepository userRepository;

    @Test
    void loadUserByUsername() {
        var expectedResult = userService.loadUserByUsername("username1");
        var resultUsername = expectedResult.getUsername();
        Assertions.assertTrue(resultUsername.equals("username1"));
    }

    @Test
    void registration() {
        RoleReadDto roleReadDto1 = new RoleReadDto("ROLE_USER");
        RoleReadDto roleReadDto2 = new RoleReadDto("ROLE_ADMIN");
        UserEditDto userEditDto = new UserEditDto("username3", "password3", List.of(roleReadDto1, roleReadDto2));
        var registration = userService.registration(userEditDto);
        var expectedUsers = userRepository.findAll();
        org.assertj.core.api.Assertions.assertThat(expectedUsers).hasSize(3);
    }
}
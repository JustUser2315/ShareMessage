package com.example.sweater_letscode.spring.service;

import com.example.sweater_letscode.spring.TestBaseApplication;
import com.example.sweater_letscode.spring.dto.RoleReadDto;
import com.example.sweater_letscode.spring.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.TestConstructor;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@RequiredArgsConstructor
@ExtendWith(MockitoExtension.class)
class RoleServiceTest extends TestBaseApplication {
    private final RoleService roleService;

    @Test
    void findAll() {
        var expectedResult = roleService.findAll();
        Assertions.assertThat(expectedResult).hasSize(3);
    }
}
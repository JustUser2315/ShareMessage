package com.example.sweater_letscode.spring.repository;

import com.example.sweater_letscode.spring.TestBaseApplication;
import com.example.sweater_letscode.spring.entity.Role;
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
class RoleRepositoryTest extends TestBaseApplication {
    private final RoleRepository roleRepository;

    @Test
    void findAllByNameIn() {
        var expectedResult = roleRepository.findAllByNameIn(List.of("ROLE_USER"));
        var expectedResult2 = roleRepository.findAllByNameIn(List.of(""));

        Assertions.assertThat(expectedResult).hasSize(1);
        Assertions.assertThat(expectedResult2).hasSize(0);
    }
}
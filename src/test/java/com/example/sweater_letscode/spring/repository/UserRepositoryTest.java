package com.example.sweater_letscode.spring.repository;

import com.example.sweater_letscode.spring.TestBaseApplication;
import com.example.sweater_letscode.spring.entity.User;
import lombok.RequiredArgsConstructor;
import org.assertj.core.api.ListAssert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.TestConstructor;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@RequiredArgsConstructor
@ExtendWith(MockitoExtension.class)
class UserRepositoryTest extends TestBaseApplication {
private final UserRepository userRepository;

    @Test
    void findByUsername() {
        var expectedResult = userRepository.findByUsername("username1");
        Assertions.assertTrue(expectedResult.isPresent());
        expectedResult.ifPresent(user-> {
            assertEquals(1L, user.getId());
            assertEquals("password1", user.getPassword());
            assertTrue(user.isActive());
        });
    }

    @Test
    void findAll(){
        var expectedResult = userRepository.findAll();
         assertThat(expectedResult).hasSize(2);

    }
}
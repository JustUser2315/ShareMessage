package com.example.sweater_letscode.spring;

import com.example.sweater_letscode.spring.entity.Role;
import com.example.sweater_letscode.spring.entity.User;
import com.example.sweater_letscode.spring.repository.RoleRepository;
import com.example.sweater_letscode.spring.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@RequiredArgsConstructor
@Transactional
@ExtendWith(MockitoExtension.class)
public class TestRunner {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Test
    void check(){
        var allByNames = roleRepository.findAllByNameIn(List.of("ROLE_USER", "ROLE_ADMIN"));
        Assertions.assertTrue(allByNames.size()==2);

    }
}

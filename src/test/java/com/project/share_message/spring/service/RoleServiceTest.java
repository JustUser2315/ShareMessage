package com.project.share_message.spring.service;

import com.project.share_message.spring.TestBaseApplication;
import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.TestConstructor;

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
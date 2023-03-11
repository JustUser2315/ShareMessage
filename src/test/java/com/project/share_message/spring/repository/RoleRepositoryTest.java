package com.project.share_message.spring.repository;

import com.project.share_message.spring.TestBaseApplication;
import com.project.share_message.spring.entity.Role;
import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestConstructor;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@RequiredArgsConstructor
@ExtendWith(MockitoExtension.class)
class RoleRepositoryTest extends TestBaseApplication {
    private final RoleRepository roleRepository;
    private final JdbcTemplate jdbcTemplate;

    @Test
    void findAllByNameIn() {
        var expectedResult = roleRepository.findAllByNameIn(List.of("ROLE_USER"));
        var expectedResult2 = roleRepository.findAllByNameIn(List.of(""));

        Assertions.assertThat(expectedResult).hasSize(1);
        Assertions.assertThat(expectedResult2).hasSize(0);
    }

    @Test
    void findRoleByName() {
        var expectedResult = roleRepository.findRoleByName("ROLE_ADMIN");
        assertTrue(expectedResult.isPresent());
        assertEquals(new Role(2, "ROLE_ADMIN"), expectedResult.get());

    }

    @Test
    void clearUserRoles() {
        var role_id = jdbcTemplate.query("select user_id from users_roles where role_id = 1", (rs, rowNum) -> rs.getObject("user_id"));
        assertTrue(role_id.size() == 1);
        roleRepository.clearUserRoles(1L, 1L);
        var expectedResult = jdbcTemplate.query("select user_id from users_roles where role_id = 1", (rs, rowNum) -> rs.getObject("user_id"));
        assertTrue(expectedResult.isEmpty());

    }
}
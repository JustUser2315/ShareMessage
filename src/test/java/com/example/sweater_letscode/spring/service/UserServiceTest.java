package com.example.sweater_letscode.spring.service;

import com.example.sweater_letscode.spring.TestBaseApplication;
import com.example.sweater_letscode.spring.dto.*;
import com.example.sweater_letscode.spring.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.TestConstructor;

import java.util.Collections;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@RequiredArgsConstructor
@ExtendWith(MockitoExtension.class)
class UserServiceTest extends TestBaseApplication {
    private final UserService userService;
    private final UserRepository userRepository;
    private final JdbcTemplate jdbcTemplate;

    @Test
    void isAccountActivated(){
        userService.doesUserHaveAnActivationCode("activation-code-example");
        var expRes = userService.findById(1L).get();
        assertThat(expRes).isNotNull();
        assertEquals(expRes.getActivationCode(), null);
        assertEquals(expRes.isActive(), true);
    }
    @Test
    void loadUserByUsername() {
        var expectedResult = userService.loadUserByUsername("username1");
        var resultUsername = expectedResult.getUsername();
        Assertions.assertTrue(resultUsername.equals("username1"));
    }

    @Test
    void registration() {
        RoleEditDto red1 = new RoleEditDto("ROLE_USER");
        UserEditDto userEditDto = new UserEditDto("test3@mail.com","username3", "rawPassword", false, "activationCode3", null,Set.of(red1));
        var registration = userService.registration(userEditDto);
        var expectedUsers = userRepository.findAll();
        assertThat(expectedUsers).hasSize(3);
    }

    @Test
    void findById(){
        var expectedResult = userService.findById(1L);

        var role_user = RoleReadDto.builder().id(1).name("ROLE_USER").build();
        var role_admin = RoleReadDto.builder().id(2).name("ROLE_ADMIN").build();
        var roles = Set.of(role_user, role_admin);

        Assertions.assertTrue(expectedResult.isPresent());
        assertEquals(expectedResult.get().getId(), 1L);
        assertEquals(expectedResult.get().getUsername(), "username1");
        Assertions.assertFalse(expectedResult.get().isActive());
        assertEquals(expectedResult.get().getRoles(), roles);

    }
    @Test
    void updateUsername(){
        RoleEditDto roleEditDto = new RoleEditDto("UNKNOWN");
        UserEditDto userEditDto = new UserEditDto(null,"username1-updated", null, true, "activationCode1", null,Set.of(roleEditDto));

        var updatedUser = userService.update(1L, userEditDto);
        var checkedUser = userService.findById(1L);
        assertEquals(updatedUser.get(), checkedUser.get());
    }
    @Test
    void updateEmail(){
        RoleEditDto roleEditDto = new RoleEditDto("UNKNOWN");
        UserEditDto userEditDto = new UserEditDto("test1update@mail.com","username2", null, false, "some-code", null,Set.of(roleEditDto));

        var beforeUpdate = userService.findById(2L).get();
        userService.update(2L, userEditDto);
        var afterUpdate = userService.findById(2L).get();
        assertNotEquals(beforeUpdate.isActive(), afterUpdate.isActive());
        assertNotEquals(beforeUpdate.getEmail(), afterUpdate.getEmail());
        assertNotEquals(beforeUpdate.getActivationCode(), afterUpdate.getActivationCode());

        assertEquals(beforeUpdate.getId(), afterUpdate.getId());
        assertEquals(beforeUpdate.getUsername(), afterUpdate.getUsername());
        System.out.println();

    }
    @Test
    void clearUserRoles(){

        var query = jdbcTemplate.query("select user_id from users_roles where user_id = 1 and role_id=1",
                (rs, rowNum) -> rs.getObject("user_id"));
        assertEquals(1, query.size());
        userService.clearUserRoles(1L, 1L);
        var query2 = jdbcTemplate.query("select user_id from users_roles where user_id = 1 and role_id=1",
                (rs, rowNum) -> rs.getObject("user_id"));
        Assertions.assertTrue(query2.isEmpty());
    }
    @Test
    void deleteUserById(){
        Assertions.assertTrue(userRepository.findById(2L).isPresent());
        userService.deleteUserById(2L);
        Assertions.assertTrue(userRepository.findById(2L).isEmpty());
    }
    @Test
    void findByUsername(){

        RoleReadDto role1 =  new RoleReadDto(1L, "ROLE_USER");
        RoleReadDto role2 =  new RoleReadDto(2L, "ROLE_ADMIN");
        MessageReadDto readDto1 = new MessageReadDto(1, "text1", "tag1", "username1", 1L, null, Collections.emptySet());
        MessageReadDto readDto2 = new MessageReadDto(3, "text3", "tag3", "username1", 1L, null, Collections.emptySet());
        MessageReadDto readDto3 = new MessageReadDto(2, "text2", "tag2", "username1", 1L, null, Collections.emptySet());
//        UserReadDto u = new UserReadDto(1L, "test1@mail.com", "username1", false, "activation-code-example",null,Collections.emptySet(), Collections.emptySet(), Collections.emptySet(), Collections.emptySet(), Collections.emptySet());
        UserReadDto u = new UserReadDto(1L, "test1@mail.com", "username1", false, "activation-code-example", null, Set.of(readDto1, readDto2, readDto3), Set.of(role1, role2),Collections.emptySet(), Collections.emptySet(), Collections.emptySet());

        var expectedResult = userService.findByUsername("username1");
        Assertions.assertTrue(expectedResult.isPresent());
        assertEquals(u, expectedResult.get());
    }
    @Test
    void updatePassword(){

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String newPassword = "1234567890";
        userService.updatePassword(2L, newPassword);
        Assertions.assertTrue(bCryptPasswordEncoder.matches(newPassword,userRepository.findById(2L).get().getPassword()));

    }

}



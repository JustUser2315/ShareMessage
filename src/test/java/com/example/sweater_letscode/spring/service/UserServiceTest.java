package com.example.sweater_letscode.spring.service;

import com.example.sweater_letscode.spring.TestBaseApplication;
import com.example.sweater_letscode.spring.dto.RoleEditDto;
import com.example.sweater_letscode.spring.dto.RoleReadDto;
import com.example.sweater_letscode.spring.dto.UserEditDto;
import com.example.sweater_letscode.spring.dto.UserFullReadDto;
import com.example.sweater_letscode.spring.repository.RoleRepository;
import com.example.sweater_letscode.spring.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.TestConstructor;

import java.util.Set;
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@RequiredArgsConstructor
@ExtendWith(MockitoExtension.class)
class UserServiceTest extends TestBaseApplication {
    private final UserService userService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JdbcTemplate jdbcTemplate;

    @Test
    void loadUserByUsername() {
        var expectedResult = userService.loadUserByUsername("username1");
        var resultUsername = expectedResult.getUsername();
        Assertions.assertTrue(resultUsername.equals("username1"));
    }

    @Test
    void registration() {
        RoleEditDto red1 = new RoleEditDto("ROLE_USER");
        RoleEditDto red2 = new RoleEditDto("ROLE_ADMIN");
        UserEditDto userEditDto = new UserEditDto("username3", "password3",true, Set.of(red1, red2));
        var registration = userService.registration(userEditDto);
        var expectedUsers = userRepository.findAll();
        org.assertj.core.api.Assertions.assertThat(expectedUsers).hasSize(3);
    }

    @Test
    void findById(){
        var expectedResult = userService.findById(1L);
        var role_user = RoleReadDto.builder().id(1).name("ROLE_USER").build();
        var role_admin = RoleReadDto.builder().id(2).name("ROLE_ADMIN").build();
        var roles = Set.of(role_user, role_admin);

        Assertions.assertTrue(expectedResult.isPresent());
        Assertions.assertEquals(expectedResult.get().getId(), 1L);
        Assertions.assertEquals(expectedResult.get().getUsername(), "username1");
        Assertions.assertTrue(expectedResult.get().isActive());
        Assertions.assertEquals(expectedResult.get().getRoles(), roles);

    }
    @Test
    void update(){
        RoleEditDto roleEditDto = new RoleEditDto("UNKNOWN");
        UserEditDto userEditDto = new UserEditDto("username1-updated",null, true, Set.of(roleEditDto));

        var updatedUser = userService.update(1L, userEditDto);
        var checkedUser = userService.findById(1L);
        Assertions.assertEquals(updatedUser.get().getUsername(), checkedUser.get().getUsername());
        Assertions.assertEquals(updatedUser.get().getId(), checkedUser.get().getId());
        Assertions.assertEquals(updatedUser.get().getRoles(), checkedUser.get().getRoles());
    }
    @Test
    void clearUserRoles(){

        var query = jdbcTemplate.query("select user_id from users_roles where user_id = 1 and role_id=1",
                (rs, rowNum) -> rs.getObject("user_id"));
        Assertions.assertEquals(1, query.size());
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
        RoleReadDto readDto =  new RoleReadDto(1L, "ROLE_USER");
        RoleReadDto readDto2 =  new RoleReadDto(2L, "ROLE_ADMIN");
        UserFullReadDto u = new UserFullReadDto(1L, "username1", true, Set.of(readDto2, readDto));

        var expectedResult = userService.findByUsername("username1");
        Assertions.assertTrue(expectedResult.isPresent());
        Assertions.assertEquals(u, expectedResult.get());
    }
    @Test
    void updatePassword(){

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String newPassword = "1234567890";
        userService.updatePassword(2L, newPassword);
        Assertions.assertTrue(bCryptPasswordEncoder.matches(newPassword,userRepository.findById(2L).get().getPassword()));

    }

}



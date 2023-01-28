package com.example.sweater_letscode.spring.controller;

import com.example.sweater_letscode.spring.TestBaseApplication;
import com.example.sweater_letscode.spring.dto.RoleReadDto;
import com.example.sweater_letscode.spring.dto.UserEditDto;
import com.example.sweater_letscode.spring.repository.RoleRepository;
import com.example.sweater_letscode.spring.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@RequiredArgsConstructor
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
class RegistrationControllerTest extends TestBaseApplication {
    private final MockMvc mockMvc;
    private final RoleService roleService;

    @Test
    void registration() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/registration"))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.model().attribute("roles", roleService.findAll()));
    }

    @Test
    void registrationNewUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/registration")
                        .param("username", "usernameTest")
                        .param("password", "passwordTest")
                        .param("roles", "rolesTest")
                        .servletPath("/registration"))
                .andExpectAll(MockMvcResultMatchers.status().is3xxRedirection(),
                              MockMvcResultMatchers.redirectedUrl("/login"));

    }
}
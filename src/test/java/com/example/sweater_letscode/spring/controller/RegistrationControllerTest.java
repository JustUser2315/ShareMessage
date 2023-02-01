package com.example.sweater_letscode.spring.controller;

import com.example.sweater_letscode.spring.TestBaseApplication;
import com.example.sweater_letscode.spring.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

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
    void registrationNewUserSuccessful() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/registration")
                        .param("username", "test@m.com")
                        .param("password", "password1"))
                .andExpectAll(MockMvcResultMatchers.status().is3xxRedirection(),
                        MockMvcResultMatchers.redirectedUrl("/login"));

    }
    @Test
    void registrationNewUserFailure() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/registration")
                        .param("username", "testUsername")
                        .param("password", "1234567"))
                .andExpectAll(MockMvcResultMatchers.status().is3xxRedirection(),
                        MockMvcResultMatchers.redirectedUrl("/registration"));

    }
}
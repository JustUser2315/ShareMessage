package com.example.sweater_letscode.spring.controller;

import com.example.sweater_letscode.spring.TestBaseApplication;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@RequiredArgsConstructor
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
class LoginControllerTest extends TestBaseApplication {
private final MockMvc mockMvc;
    @Test
    @WithMockUser(username = "username2", password = "password2")
    void loginPage() throws Exception {
        mockMvc.perform(get("/messages"))
                .andExpectAll(status().is2xxSuccessful(),
                view().name("messages"));

    }
}
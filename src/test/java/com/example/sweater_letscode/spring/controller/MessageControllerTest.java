package com.example.sweater_letscode.spring.controller;

import com.example.sweater_letscode.spring.TestBaseApplication;
import com.example.sweater_letscode.spring.filter.MessageFilter;
import com.example.sweater_letscode.spring.filter.PageResponse;
import com.example.sweater_letscode.spring.repository.MessageRepository;
import com.example.sweater_letscode.spring.repository.UserRepository;
import com.example.sweater_letscode.spring.service.MessageService;
import com.example.sweater_letscode.spring.service.UserService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor
class MessageControllerTest extends TestBaseApplication {
    private final MockMvc mockMvc;
    private final UserService userService;

    @Test
    @WithMockUser(username = "username1", password = "password1")
    void findAll() throws Exception {
        MessageFilter messageFilter = new MessageFilter(null, null);
        mockMvc.perform(MockMvcRequestBuilders.get("/messages"))
                .andExpect(MockMvcResultMatchers.model().attribute("filter", messageFilter))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.view().name("messages"));
    }

    @Test
    void save() throws Exception {
        var loadedUser = userService.loadUserByUsername("username1");
        mockMvc.perform(MockMvcRequestBuilders.post("/messages")
                        .with(SecurityMockMvcRequestPostProcessors.user(loadedUser))
                .param("text", "TestText")
                .param("tag", "TestTag")
        ).andExpectAll(MockMvcResultMatchers.status().is3xxRedirection(),
                MockMvcResultMatchers.redirectedUrl("/messages"));

    }
}
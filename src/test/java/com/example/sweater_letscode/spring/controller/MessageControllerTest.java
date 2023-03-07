package com.example.sweater_letscode.spring.controller;

import com.example.sweater_letscode.spring.TestBaseApplication;
import com.example.sweater_letscode.spring.filter.MessageFilter;
import com.example.sweater_letscode.spring.service.RoleService;
import com.example.sweater_letscode.spring.service.UserService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor
@WithMockUser(username = "username1", password = "password1", authorities = {"ROLE_ADMIN"})
class MessageControllerTest extends TestBaseApplication {
    private final MockMvc mockMvc;
    private final UserService userService;
    private final RoleService roleService;

    @Test
    void findAll() throws Exception {
        var role_admin = roleService.findAll().stream().filter(roleReadDto -> roleReadDto.getName().equals("ROLE_ADMIN")).findFirst().get();
        var user = userService.findByUsername(userService.usernameFromContext());
        MessageFilter messageFilter = new MessageFilter(null, null);
        mockMvc.perform(get("/messages"))
                .andExpectAll(model().attribute("filter", messageFilter),
                        model().attribute("role", role_admin),
                        model().attribute("userICheck", user),
                        status().is2xxSuccessful(),
                        view().name("messages"));
    }

    @Test
    void save() throws Exception {
        var loadedUser = userService.loadUserByUsername("username1");
        mockMvc.perform(post("/messages")
                .with(SecurityMockMvcRequestPostProcessors.user(loadedUser))
                .param("text", "TestText")
                .param("tag", "TestTag")
        ).andExpectAll(status().is3xxRedirection(),
                redirectedUrl("/messages"));
    }

    @Test
    void delete() throws Exception {
        mockMvc.perform(post("/messages/{id}/delete_message", 1))
                .andExpectAll(status().is3xxRedirection(),
                        redirectedUrl("/messages"));

    }
 
}
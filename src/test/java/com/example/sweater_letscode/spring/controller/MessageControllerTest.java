package com.example.sweater_letscode.spring.controller;

import com.example.sweater_letscode.spring.TestBaseApplication;
import com.example.sweater_letscode.spring.dto.RoleReadDto;
import com.example.sweater_letscode.spring.filter.MessageFilter;
import com.example.sweater_letscode.spring.service.RoleService;
import com.example.sweater_letscode.spring.service.UserService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Disabled;
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
    void showAllWithFilter() throws Exception {
        var role_admin =  new RoleReadDto(2L, "ROLE_ADMIN");
        var user = userService.findByUsername(userService.usernameFromContext()).get();
        MessageFilter messageFilter = new MessageFilter(null, null);
        mockMvc.perform(get("/messages"))
                .andExpectAll(model().attribute("filter", messageFilter),
                        model().attribute("user", user),
                        model().attribute("role", role_admin),
                        status().is2xxSuccessful(),
                        view().name("messages"));

    }

    @Test
    @Disabled
    void save() throws Exception {
        var loadedUser = userService.loadUserByUsername("username1");
        mockMvc.perform(post("/message/create")
                .with(SecurityMockMvcRequestPostProcessors.user(loadedUser))
                .param("text", "TestText")
                .param("tag", "tag8")
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
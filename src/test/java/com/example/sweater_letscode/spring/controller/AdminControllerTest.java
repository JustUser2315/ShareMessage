package com.example.sweater_letscode.spring.controller;

import com.example.sweater_letscode.spring.TestBaseApplication;
import com.example.sweater_letscode.spring.service.RoleService;
import com.example.sweater_letscode.spring.service.UserService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@WithMockUser(username = "username1", password = "password1", authorities = {"ROLE_ADMIN"})
class AdminControllerTest extends TestBaseApplication {
    private final MockMvc mockMvc;
    private final RoleService roleService;
    private final UserService userService;
    private final WebApplicationContext webApplicationContext;

    @Test
    void showAllUsersWithFilter() throws Exception {
        mockMvc.perform(get("/admin/users"))
                .andExpectAll(status().is2xxSuccessful(),
                        view().name("admin/users"),
                        model().attributeExists("users", "filter"));
    }

    @Test
    void userInfo() throws Exception {
        mockMvc.perform(get("/admin/users/1"))
                .andExpectAll(model().attributeExists("roles", "user"),
                        view().name("admin/user"),
                        status().is2xxSuccessful(),
                        model().attribute("roles", roleService.findAll()),
                        model().attribute("user", userService.findById(1L).get()));
    }

    @Test
    void update() throws Exception {
        mockMvc.perform(post("/admin/users/1")
                        .param("username", "someData")
                        .param("active", "someData")
                        .param("roles", "UNKNOWN"))

                .andExpectAll(redirectedUrl("/admin/users/1"),
                        status().is3xxRedirection());
    }

    @Test
    void clearUserRoles() throws Exception {
        mockMvc.perform(post("/admin/users/1/delete_role/1"))
                .andExpectAll(redirectedUrl("/admin/users/1"),
                        status().is3xxRedirection());

    }

    @Test
    void deleteUser() throws Exception {
        mockMvc.perform(post("/admin/users/1/delete_user"))
                .andExpectAll(redirectedUrl("/admin/users"),
                        status().is3xxRedirection());

    }
}
package com.example.sweater_letscode.spring.controller;

import com.example.sweater_letscode.spring.TestBaseApplication;
import com.example.sweater_letscode.spring.service.MessageService;
import com.example.sweater_letscode.spring.service.UserService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@WithMockUser(username = "username1", password = "password1", authorities = {"ROLE_ADMIN"})
class UserControllerTest extends TestBaseApplication {
    private final MockMvc mockMvc;
    private final UserService userService;
    private final MessageService messageService;

    @Test
    void myProfile() throws Exception {
        mockMvc.perform(get("/user/1/profile"))
                .andExpectAll(
                        view().name("user/profile"),
                        status().is2xxSuccessful(),
                        model().attribute("user", userService.findById(1L).get())
                );
    }

    @Test
    void updateMyAccount() throws Exception {
        mockMvc.perform(post("/user/1/profile")
                .param("username", "username1-updated"))
                .andExpectAll(
                        status().is3xxRedirection(),
                        redirectedUrl("/login")
                );
    }

    @Test
    void deleteMyAccount() throws Exception {
        mockMvc.perform(post("/user/{id}/profile/delete_account", 1, 2))
                .andExpectAll(status().is3xxRedirection(),
                        redirectedUrl("/login"));

    }

    @Test
    void updateMyPassword() throws Exception {
        mockMvc.perform(get("/user/{id}/profile/update_password", 1))
                .andExpectAll(
                        status().is2xxSuccessful(),
                        model().attribute("user", userService.findById(1L).get())
                );

    }

    @Test
    void testUpdateMyPassword() throws Exception {
        mockMvc.perform(post("/user/{id}/profile/update_password", 1,2)
                        .param("newPassword", "password-updated")
                        .param("confirmPassword", "password-updated"))
                .andExpectAll(
                        status().is3xxRedirection(),
                        redirectedUrl("/login")
                );
    }

    @Test
    void seeMyMessages() throws Exception {
        mockMvc.perform(get("/user/{id}/profile/messages", 1))
                .andExpectAll(
                  status().is2xxSuccessful(),
                  view().name("user/messages"),
                        model().attribute("user",userService.findById(1L).get()),
                        model().attribute("userMessages",messageService.findAllByAuthorId(1L))
                );
    }

    @GetMapping("/activate/{code}")
    public String activate(@PathVariable String code) {
        var accountActivated = userService.doesUserHaveAnActivationCode(code);
        if(accountActivated){
            return "/login";
        }
        return "redirect:/registration";
    }

    @Test
    void activate() throws Exception {
//        var beforeLinkClick = userService.findById(1L).get();
        mockMvc.perform(get("/user/activate/{code}", "activation-code-example"))
                .andExpectAll(status().is3xxRedirection(), redirectedUrl("/login"));

        var afterLinkClick = userService.findById(1L).get();
        Assertions.assertTrue(afterLinkClick.isActive());
        Assertions.assertNull(afterLinkClick.getActivationCode());

    }
}

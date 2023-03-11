package com.project.share_message.spring.controller;

import com.project.share_message.spring.TestBaseApplication;
import com.project.share_message.spring.dto.RoleReadDto;
import com.project.share_message.spring.repository.UserRepository;
import com.project.share_message.spring.service.MessageService;
import com.project.share_message.spring.service.UserService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
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
    private final UserRepository userRepository;

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
    @Disabled
    void deleteMyProfile() throws Exception {
        mockMvc.perform(post("/{id}/profile/delete_account", 1)
                        .param("confirm", "Yes")
                        .param("g-recaptcha-response", ""))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    void updateMyPasswordGet() throws Exception {
        mockMvc.perform(get("/user/{id}/profile/update_password", 1))
                .andExpectAll(
                        status().is2xxSuccessful(),
                        model().attribute("user", userService.findById(1L).get())
                );

    }

    @Test
    @Disabled
    void updateMyPasswordPost() throws Exception {
        mockMvc.perform(post("/user/{id}/profile/update_password", 1)
                        .param("newPassword", "password-updated")
                        .param("confirmPassword", "password-updated")
                        .param("oldPassword", userRepository.findById(1L).get().getPassword()))
                .andExpectAll(
                        status().is3xxRedirection(),
                        redirectedUrl("/login")
                );
    }

    @Test
    void showMyMessages() throws Exception {
        var role_admin = new RoleReadDto(2L, "ROLE_ADMIN");
        mockMvc.perform(get("/user/{id}/profile/messages", 1))
                .andExpectAll(
                  status().is2xxSuccessful(),
                  view().name("user/messages"),
                        model().attribute("user",userService.findById(1L).get()),
                        model().attribute("messages",messageService.findAllByAuthorId(1L)),
                        model().attribute("role", role_admin)
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
        mockMvc.perform(get("/user/activate/{code}", "activation-code-example"))
                .andExpectAll(status().is3xxRedirection(), redirectedUrl("/login"));

        var afterLinkClick = userService.findById(1L).get();
        Assertions.assertTrue(afterLinkClick.isActive());
        Assertions.assertNull(afterLinkClick.getActivationCode());

    }
}

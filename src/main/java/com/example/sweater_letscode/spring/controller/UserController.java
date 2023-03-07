package com.example.sweater_letscode.spring.controller;

import com.example.sweater_letscode.spring.dto.MessageReadDto;
import com.example.sweater_letscode.spring.dto.UpdatePasswordEditDto;
import com.example.sweater_letscode.spring.dto.UserEditDto;
import com.example.sweater_letscode.spring.dto.UserReadDto;
import com.example.sweater_letscode.spring.entity.User;
import com.example.sweater_letscode.spring.recaptcha.RecaptchaRegisterService;
import com.example.sweater_letscode.spring.recaptcha.RecaptchaResponse;
import com.example.sweater_letscode.spring.service.MessageService;
import com.example.sweater_letscode.spring.service.RoleService;
import com.example.sweater_letscode.spring.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final MessageService messageService;
    private final UserService userService;
    private final RecaptchaRegisterService recaptchaRegisterService;
    private final RoleService roleService;
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    /*
    Viewing and updating the account by the userICheck himself.
    GET and POST forms
    */
    @GetMapping("/{id}/profile")
    public String myProfile(Model model, @PathVariable Long id) {
        var role_admin = roleService.findAll().stream().filter(roleReadDto -> roleReadDto.getName().equals("ROLE_ADMIN")).findFirst().get();
        var mbUser = userService.findById(id).get();
        model.addAttribute("user", mbUser);
        model.addAttribute("subscriptions", mbUser.getSubscriptions());
        model.addAttribute("role", role_admin);
        return "user/profile";
    }


    @PostMapping("/{id}/profile")
    public String updateMyProfile(@PathVariable Long id, @Valid @ModelAttribute UserEditDto userEditDto, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if(bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            redirectAttributes.addFlashAttribute("user", userEditDto);
            return "redirect:/user/%d/profile".formatted(id);
        }
        userService.update(id, userEditDto);
        return "redirect:/login";
    }

    /*
    Deleting account by userICheck himself
     */
    @GetMapping("/{id}/profile/delete_account")
    public String delete(@PathVariable Long id, Model model) {
        UserReadDto mbUser = userService.findById(id).get();
        model.addAttribute("user", mbUser);
        return "user/delete";
    }

    @PostMapping("/{id}/profile/delete_account")
    public String deleteMyProfile(@PathVariable Long id, @RequestParam String confirm, HttpServletRequest request,
                                  @RequestParam(name = ("g-recaptcha-response")) String reCaptchaResponse) throws IOException {
        log.info(">>>>>>>>>>>>>>>>>>>>>>>> g-recaptcha-response: {}", reCaptchaResponse);
        RecaptchaResponse verify = recaptchaRegisterService.verify(reCaptchaResponse);
        if (confirm.equals("Yes") && verify.isSuccess()) {
            userService.deleteUserById(id);
            return "redirect:/login";
        }
        String prevLink = request.getHeader("referer") != null ? request.getHeader("referer") : "http://localhost:8080/main";
        return "redirect:/user/%d/profile".formatted(id);
    }

    /*
    Account's password updating with confirming.
    GET and POST forms
    */
    @GetMapping("/{id}/profile/update_password")
    public String updateMyPassword(@PathVariable Long id, Model model, UpdatePasswordEditDto updatePasswordEditDto) {
        UserReadDto mbUser = userService.findById(id).get();
        model.addAttribute("user", mbUser);
        model.addAttribute("password", updatePasswordEditDto);
        return "update_user_password";
    }


    @PostMapping("/{id}/profile/update_password")
    public String updateMyPassword(@PathVariable Long id, @ModelAttribute @Valid UpdatePasswordEditDto updatePasswordEditDto,
                                   BindingResult bindingResult,
                                   RedirectAttributes redirectAttributes,
                                   @AuthenticationPrincipal User user,
                                   @RequestParam(name = "g-recaptcha-response") String reCaptchaResponse) {
        boolean passwordMatch = new BCryptPasswordEncoder().matches(updatePasswordEditDto.getOldPassword(), user.getPassword());
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> g-recaptcha-response: {}", reCaptchaResponse);
        RecaptchaResponse verify = recaptchaRegisterService.verify(reCaptchaResponse);
        if(bindingResult.hasErrors() || !verify.isSuccess() || (bindingResult.hasErrors() && !verify.isSuccess())){
            redirectAttributes.addFlashAttribute("reCaptchaErrors", verify.getAllErrors());
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            redirectAttributes.addFlashAttribute("password", updatePasswordEditDto);
            return "redirect:/user/%d/profile/update_password".formatted(id);
        }

        if (updatePasswordEditDto.getNewPassword().equals(updatePasswordEditDto.getConfirmPassword()) && passwordMatch && verify.isSuccess()) {
            userService.updatePassword(id, updatePasswordEditDto.getNewPassword());
            return "redirect:/login";
        }
        return "redirect:/{id}/profile/update_password?error=password";
    }

    /*
    User's messages viewing and deleting by himself
    */
    @GetMapping("/{id}/profile/messages")
    public String showMyMessages(@PathVariable Long id, Model model) {
        var role_admin = roleService.findAll().stream().filter(roleReadDto -> roleReadDto.getName().equals("ROLE_ADMIN")).findFirst().get();
        var user = userService.findById(id).get();
        var messages = messageService.findAllByAuthorId(id);
        model.addAttribute("messages", messages);
        model.addAttribute("user", user);
        model.addAttribute("role", role_admin);
        return "user/messages";
    }

    /*
    Transiting page for code activation process
     */
    @GetMapping("/activate/{code}")
    public String activate(@PathVariable String code) {
        var isAccountActivated = userService.doesUserHaveAnActivationCode(code);
        return isAccountActivated
                ? "redirect:/login"
                : "redirect:/registration";
    }

    /*
    Page for the avatar image
    */
    @GetMapping("/{id}/profile/avatar")
    @ResponseBody()
    public byte[] findAvatar(@PathVariable Long id) throws IOException {
        return userService.findAvatar(id)
                .get();

    }

    @GetMapping("/{id}/profile/subscriptions")
    public String subscriptions(@PathVariable Long id, Model model) {
        var me = userService.findById(id).get();
        model.addAttribute("user", me);
        model.addAttribute("subscribtions", me.getSubscriptions());
        return "user/subscriptions";
    }

    @GetMapping("/{id}/profile/subscribers")
    public String subscribers(@PathVariable Long id, Model model) {
        var me = userService.findById(id).get();
        model.addAttribute("user", me);
        model.addAttribute("subscribers", me.getSubscribers());
        return "user/subscribers";
    }

    @GetMapping("/{id}/profile/show")
    public String showProfile(Model model, @PathVariable Long id) {
        var role_admin = roleService.findAll().stream().filter(roleReadDto -> roleReadDto.getName().equals("ROLE_ADMIN")).findFirst().get();
        List<MessageReadDto> messages = messageService.findAllByAuthorId(id);
        var userWhichPageILookAt = userService.findById(id).get();
        var me = userService.findByUsername(userService.usernameFromContext()).get();
        boolean alreadySub = userService.isAlreadySub(me.getId(), id);
        model.addAttribute("userWhichPageILookAt", userWhichPageILookAt);
        model.addAttribute("user", me);
        model.addAttribute("messages", messages);
        model.addAttribute("isSub", alreadySub);
        model.addAttribute("role", role_admin);
        return "user/see_data";
    }

    @PostMapping("/{id}/profile/subscribe")
    public String subscribe(Model model, @PathVariable Long id) {
        //userId = channelId, id = whoFollowId
        var user = userService.findByUsername(userService.usernameFromContext()).get();
        if (user.getId() != null && id != null) {
            userService.subscribe(user.getId(), id);
            return "redirect:/messages";
        }
        return "user/see_data";
    }

    @PostMapping("/{id}/profile/unsubscribe")
    public String unsubscribe(Model model, @PathVariable Long id) {
        var user = userService.findByUsername(userService.usernameFromContext()).get();
        if (user.getId() != null && id != null) {
            userService.unsubscribe(user.getId(), id);
            return "redirect:/messages";
        }
        return "user/see_data";
    }


}

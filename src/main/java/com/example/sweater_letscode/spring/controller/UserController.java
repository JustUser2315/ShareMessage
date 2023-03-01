package com.example.sweater_letscode.spring.controller;

import com.example.sweater_letscode.spring.dto.MessageReadDto;
import com.example.sweater_letscode.spring.dto.UserEditDto;
import com.example.sweater_letscode.spring.service.MessageService;
import com.example.sweater_letscode.spring.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final MessageService messageService;
    private final UserService userService;

    /*
    Viewing and updating the account by the user himself.
    GET and POST forms
    */
    @GetMapping("/{id}/profile")
    public String myProfile(Model model, @PathVariable Long id) {
        var mbUser = userService.findById(id).get();
        model.addAttribute("user", mbUser);
        model.addAttribute("subscriptions", mbUser.getSubscriptions());

        return "user/profile";
    }
    @GetMapping("/{id}/profile/subscriptions")
    public String subscriptions(@PathVariable Long id, Model model){
        var mbUser = userService.findById(id).get();
        model.addAttribute("user", mbUser);
        model.addAttribute("subscribtions", mbUser.getSubscriptions());
        return "user/subscriptions";
    }
    @GetMapping("/{id}/profile/subscribers")
    public String subscribers(@PathVariable Long id, Model model){
        var mbUser = userService.findById(id).get();
        model.addAttribute("user", mbUser);
        model.addAttribute("subscribers", mbUser.getSubscribers());
        return "user/subscribers";
    }

    @PostMapping("/{id}/profile")
    public String updateMyProfile(@PathVariable Long id, @ModelAttribute UserEditDto userEditDto) {
        userService.update(id, userEditDto);
        return "redirect:/login";
    }

    /*
    Deleting account by user himself
     */
    @PostMapping("/{id}/profile/delete_account")
    public String deleteMyProfile(@PathVariable Long id) {
        userService.deleteUserById(id);
        return "redirect:/login";
    }

    /*
    Account's password updating with confirming.
    GET and POST forms
    */
    @GetMapping("/{id}/profile/update_password")
    public String updateMyPassword(Model model, @PathVariable Long id) {
        model.addAttribute("user", userService.findById(id).get());
        return "update_user_password";
    }


    @PostMapping("/{id}/profile/update_password")
    public String updateMyPassword(@PathVariable Long id, @RequestParam String newPassword, @RequestParam String confirmPassword) {
        if (newPassword.equals(confirmPassword)) {
            userService.updatePassword(id, newPassword);
            return "redirect:/login";
        }
        return "redirect:/{id}/profile/update_password?error=password";
    }

    /*
    User's messages viewing and deleting by himself
    */
    @GetMapping("/{id}/profile/messages")
    public String showMyMessages(@PathVariable Long id, Model model) {
        var user = userService.findById(id).get();
        var userMessages = messageService.findAllByAuthorId(id);
        model.addAttribute("userMessages", userMessages);
        model.addAttribute("user", user);

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
    @GetMapping("/{id}/profile/show")
    public String showProfile(Model model, @PathVariable Long id) {
        List<MessageReadDto> messages = messageService.findAllByAuthorId(id);
        var user = userService.findByUsername(userService.usernameFromContext()).get();
        var mbUser = userService.findById(id).get();
        boolean alreadySub = userService.isAlreadySub(user.getId(), id);
model.addAttribute("userToSeeId", id);
        model.addAttribute("user", mbUser);
        model.addAttribute("messages", messages);
        model.addAttribute("isSub", alreadySub);
        return "user/see_data";
    }
    @PostMapping("/{id}/profile/subscribe")
    public String subscribe(Model model, @PathVariable Long id) {
        var user = userService.findByUsername(userService.usernameFromContext()).get();
        if(user.getId()!=null && id!=null){
        userService.subscribe(user.getId(), id);
        return "redirect:/messages";
        }
        return "user/see_data";
    }
    @PostMapping("/{id}/profile/unsubscribe")
    public String unsubscribe(Model model, @PathVariable Long id) {
        var user = userService.findByUsername(userService.usernameFromContext()).get();
        if(user.getId()!=null && id!=null){
            userService.unsubscribe(user.getId(), id);
            return "redirect:/messages";
        }
        return "user/see_data";
    }


}

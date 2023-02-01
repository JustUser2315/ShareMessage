package com.example.sweater_letscode.spring.controller;

import com.example.sweater_letscode.spring.dto.UserEditDto;
import com.example.sweater_letscode.spring.service.MessageService;
import com.example.sweater_letscode.spring.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final MessageService messageService;
    private final UserService userService;

    @GetMapping("/{id}/profile")
    public String myProfile(Model model, @PathVariable Long id){
        var mbUser = userService.findById(id).get();
        model.addAttribute("user", mbUser);
        return "user/profile";
    }

    @PostMapping("/{id}/profile")
    public String updateMyAccount(@PathVariable Long id, @ModelAttribute UserEditDto userEditDto){
        userService.update(id,userEditDto);
        return "redirect:/login";
    }

    @PostMapping("/{id}/profile/delete_account")
    public String deleteMyAccount(@PathVariable Long id){
        userService.deleteUserById(id);
        return "redirect:/login";
    }

    @GetMapping("/{id}/profile/update_password")
    public String updateMyPassword(Model model, @PathVariable Long id){
        model.addAttribute("user", userService.findById(id).get());
        return "update_user_password";
    }


    @PostMapping("/{id}/profile/update_password")
    public String updateMyPassword(@PathVariable Long id, @RequestParam String newPassword, @RequestParam String confirmPassword){
        if(newPassword.equals(confirmPassword)){
            userService.updatePassword(id, newPassword);
        return "redirect:/login";
        }
        return "redirect:/{id}/profile/update_password?error=password";
    }
    @GetMapping("/{id}/profile/messages")
    public String seeMyMessages(@PathVariable Long id, Model model){
        var user = userService.findById(id).get();
        var userMessages =  messageService.findAllByAuthorId(id);
       model.addAttribute("userMessages", userMessages);
       model.addAttribute("user", user);

        return "user/messages";
        }




}

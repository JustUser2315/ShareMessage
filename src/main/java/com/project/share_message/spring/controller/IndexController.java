package com.project.share_message.spring.controller;

import com.project.share_message.spring.dto.UserReadDto;
import com.project.share_message.spring.service.ImageService;
import com.project.share_message.spring.service.MessageService;
import com.project.share_message.spring.service.RoleService;
import com.project.share_message.spring.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

@Controller
@RequestMapping("/main")
@RequiredArgsConstructor
public class IndexController {
    private final MessageService messageService;
    private final ImageService imageService;
    private final RoleService roleService;
    private final UserService userService;
    @GetMapping
    public String index(Model model){
        if(!SecurityContextHolder.getContext().getAuthentication().getName().equals("anonymousUser")){
        UserReadDto user = userService.findByUsername(userService.usernameFromContext()).get();
        model.addAttribute("user", user);
        }
        var role_admin = roleService.findAll().stream().filter(roleReadDto -> roleReadDto.getName().equals("ROLE_ADMIN")).findFirst().get();
        model.addAttribute("role", role_admin);
        model.addAttribute("messages", messageService.showLastTen());
        return "main";
    }

    @GetMapping("/logo")
    @ResponseBody
    public byte[] logo()  {
        try {
            return imageService.loadLogo("logo.png").get();
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT);
        }
    }
}

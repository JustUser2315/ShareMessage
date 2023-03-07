package com.example.sweater_letscode.spring.controller;

import com.example.sweater_letscode.spring.dto.UserReadDto;
import com.example.sweater_letscode.spring.service.ImageService;
import com.example.sweater_letscode.spring.service.MessageService;
import com.example.sweater_letscode.spring.service.RoleService;
import com.example.sweater_letscode.spring.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
    public byte[] logo() throws IOException {
      return imageService.loadLogo("10.png").get();
    }
}

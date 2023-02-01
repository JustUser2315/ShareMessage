package com.example.sweater_letscode.spring.controller;

import com.example.sweater_letscode.spring.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/login")
@RequiredArgsConstructor
public class LoginController {
    private final UserService userService;
    @GetMapping
    public String loginPage(){
        return "login";
    }

}

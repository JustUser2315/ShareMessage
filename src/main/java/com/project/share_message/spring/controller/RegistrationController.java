package com.project.share_message.spring.controller;

import com.project.share_message.spring.dto.UserEditDto;
import com.project.share_message.spring.recaptcha.RecaptchaResponse;
import com.project.share_message.spring.service.EmailServiceImpl;
import com.project.share_message.spring.recaptcha.RecaptchaRegisterService;
import com.project.share_message.spring.service.RoleService;
import com.project.share_message.spring.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/registration")
@RequiredArgsConstructor

public class RegistrationController {
    private final UserService userService;
    private final RoleService roleService;
    private final RecaptchaRegisterService recaptchaRegisterService;
    private final EmailServiceImpl emailService;
    private static final Logger log = LoggerFactory.getLogger(RegistrationController.class);


    @GetMapping
    public String registration(Model model){
        model.addAttribute("roles", roleService.findAll());
        return "registration";
    }
    @PostMapping
    public String registration(@ModelAttribute @Valid UserEditDto userEditDto, BindingResult bindingResult, RedirectAttributes redirectAttributes,
                               @RequestParam(name = "g-recaptcha-response") String reCaptchaResponse){


        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> g-recaptcha-response: {}", reCaptchaResponse);

        RecaptchaResponse verify = recaptchaRegisterService.verify(reCaptchaResponse);
        if(bindingResult.hasErrors() || !verify.isSuccess() || (bindingResult.hasErrors() && !verify.isSuccess())){
            redirectAttributes.addFlashAttribute("reCaptchaErrors", verify.getAllErrors());
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            redirectAttributes.addFlashAttribute("user", userEditDto);
            return "redirect:/registration";
        }
        userService.registration(userEditDto);
        return "email";
    }



}

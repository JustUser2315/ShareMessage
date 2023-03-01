package com.example.sweater_letscode.spring.controller;

import com.example.sweater_letscode.spring.dto.MessageEditDto;
import com.example.sweater_letscode.spring.filter.MessageFilter;
import com.example.sweater_letscode.spring.filter.PageResponse;
import com.example.sweater_letscode.spring.service.MessageService;
import com.example.sweater_letscode.spring.service.RoleService;
import com.example.sweater_letscode.spring.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;

@Controller
@RequestMapping("/messages")
@RequiredArgsConstructor
public class MessageController {
   private final MessageService messageService;
   private final UserService userService;
   private final RoleService roleService;

    @GetMapping
    public String showAllWithFilter(Model model, MessageFilter filter, Pageable pageable){
        var role_admin = roleService.findAll().stream().filter(roleReadDto -> roleReadDto.getName().equals("ROLE_ADMIN")).findFirst().get();
        var allWithFilter = messageService.findAllWithFilter(filter, pageable);
        var user = userService.findByUsername(userService.usernameFromContext());
        model.addAttribute("messages", PageResponse.createPageResponse(allWithFilter));
        model.addAttribute("filter", filter);
        model.addAttribute("user", user);
        model.addAttribute("role", role_admin);
        return "messages";
    }
    @PostMapping
    public String save(@ModelAttribute @Valid MessageEditDto messageEditDto, BindingResult bindingResult, RedirectAttributes redirectAttributes){
        if(bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/messages";
        }
        messageService.save(messageEditDto);
        return "redirect:/messages";
    }
    @PostMapping ("/{id}/delete_message")
    public String delete(@PathVariable Integer id){
        String username = userService.usernameFromContext();
        Long userId = userService.findByUsername(username).get().getId();
        messageService.delete(id);
        return "redirect:/user/%d/profile/messages".formatted(userId);
    }
    @PostMapping ("/{id}/update_message")
    public String update(@PathVariable Integer id, @ModelAttribute MessageEditDto messageEditDto){
        String username = userService.usernameFromContext();
        Long userId = userService.findByUsername(username).get().getId();
        messageService.update(id, messageEditDto);
        return "redirect:/user/%d/profile/messages".formatted(userId);
    }

    @GetMapping("/{id}/picture")
    @ResponseBody()
    public byte[] messagePicture(@PathVariable Integer id) throws IOException, IOException {
        return messageService.findPicture(id).get();
    }
}

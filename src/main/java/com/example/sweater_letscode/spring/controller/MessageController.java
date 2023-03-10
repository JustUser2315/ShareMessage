package com.example.sweater_letscode.spring.controller;

import com.example.sweater_letscode.spring.dto.MessageEditDto;
import com.example.sweater_letscode.spring.dto.MessageReadDto;
import com.example.sweater_letscode.spring.dto.UserReadDto;
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
    public String showAllWithFilter(Model model, MessageFilter filter, Pageable pageable) {
        var role_admin = roleService.findAll().stream().filter(roleReadDto -> roleReadDto.getName().equals("ROLE_ADMIN")).findFirst().get();
        UserReadDto user = userService.findByUsername(userService.usernameFromContext()).get();
        var allWithFilter = messageService.findAllWithFilter(filter, pageable);
        var notLiked = PageResponse.createPageResponse(allWithFilter).getContent().stream().map(MessageReadDto::getId)
                .filter(it -> !messageService.isLiked(user.getId(), it))
                .toList();
        model.addAttribute("messages", PageResponse.createPageResponse(allWithFilter));
        model.addAttribute("filter", filter);
        model.addAttribute("user", user);
        model.addAttribute("role", role_admin);
        model.addAttribute("notLiked", notLiked);
        return "messages";
    }

    @GetMapping("/create")
    public String create(Model model, MessageEditDto messageEditDto) {
        UserReadDto user = userService.findByUsername(userService.usernameFromContext()).get();
        var role_admin = roleService.findAll().stream().filter(roleReadDto -> roleReadDto.getName().equals("ROLE_ADMIN")).findFirst().get();
        model.addAttribute("user", user);
        model.addAttribute("role", role_admin);
        model.addAttribute("message", messageEditDto);
        return "create_message";
    }

    @PostMapping("/create")
    public String save(@ModelAttribute @Valid MessageEditDto messageEditDto, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            redirectAttributes.addFlashAttribute("message", messageEditDto);
            return "redirect:/messages/create";
        }
        messageService.save(messageEditDto);
        return "redirect:/messages";
    }

    @PostMapping("/{id}/delete_message")
    public String delete(@PathVariable Integer id) {
        String username = userService.usernameFromContext();
        messageService.delete(id);
        return "redirect:/messages";
    }

    @GetMapping("/{id}/update_message")
    public String update(@PathVariable Integer id, Model model) {
        MessageReadDto message = messageService.findById(id).get();
        UserReadDto user = userService.findByUsername(userService.usernameFromContext()).get();
        model.addAttribute("user", user);
        model.addAttribute("message", message);
        return "user/update_message";
    }

    @PostMapping("/{id}/update_message")
    public String update(@PathVariable Integer id, @ModelAttribute @Valid MessageEditDto messageEditDto, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        String username = userService.usernameFromContext();
        Long userId = userService.findByUsername(username).get().getId();
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            redirectAttributes.addFlashAttribute("message", messageEditDto);
            return "redirect:/messages/%d/update_message".formatted(id);
        }
        messageService.update(id, messageEditDto);
        return "redirect:/messages";
    }

    @GetMapping("/{id}/picture")
    @ResponseBody()
    public byte[] messagePicture(@PathVariable Integer id) throws IOException, IOException {
        return messageService.findPicture(id).get();
    }

}

package com.example.sweater_letscode.spring.controller;

import com.example.sweater_letscode.spring.dto.MessageEditDto;
import com.example.sweater_letscode.spring.dto.MessageReadDto;
import com.example.sweater_letscode.spring.entity.User;
import com.example.sweater_letscode.spring.filter.MessageFilter;
import com.example.sweater_letscode.spring.filter.PageResponse;
import com.example.sweater_letscode.spring.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/messages")
@RequiredArgsConstructor
public class MessageController {
   private final MessageService messageService;
    @GetMapping
    public String findAll(Model model, MessageFilter filter, Pageable pageable){
        var allWithFilter = messageService.findAllWithFilter(filter, pageable);

        model.addAttribute("messages", PageResponse.createPageResponse(allWithFilter));
        model.addAttribute("filter", filter);
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
}

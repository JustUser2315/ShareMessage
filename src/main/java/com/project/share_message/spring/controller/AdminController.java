package com.project.share_message.spring.controller;

import com.project.share_message.spring.dto.RoleReadDto;
import com.project.share_message.spring.dto.UserEditDto;
import com.project.share_message.spring.dto.UserReadDto;
import com.project.share_message.spring.filter.PageResponse;
import com.project.share_message.spring.filter.UserFilter;
import com.project.share_message.spring.service.RoleService;
import com.project.share_message.spring.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/users")
@RequiredArgsConstructor
@Slf4j
public class AdminController {
    private final UserService userService;
    private final RoleService roleService;
    @GetMapping
    public String showAllUsersWithFilter(Model model, Pageable pageable, UserFilter filter) {
        var me = userService.findByUsername(userService.usernameFromContext()).get();
        var pageResponse = PageResponse.createPageResponse(userService.showAllUsersWithFilter(filter, pageable));
        Set<String> roles = pageResponse.getContent().stream()
                .map(UserReadDto::getRoles)
                .flatMap(Collection::stream)
                .map(RoleReadDto::getName)
                .collect(Collectors.toSet());
        model.addAttribute("users", pageResponse);
        model.addAttribute("filter", filter);
        model.addAttribute("user", me);
        model.addAttribute("roles", roles);
        return "admin/users";
    }
    @GetMapping("/{id}")
    public String checkUserInfo(@PathVariable Long id, Model model){
        Optional<UserReadDto> userICheck = userService.findById(id);
        UserReadDto me = userService.findByUsername(userService.usernameFromContext()).get();

        if(userICheck.isPresent()){
        model.addAttribute("userICheck",userICheck.get());
        }
        model.addAttribute("roles", roleService.findAll());
        model.addAttribute("user", me);
        return "admin/user";
    }
    @PostMapping("/{id}")
    public String editUserData(@PathVariable Long id, @ModelAttribute UserEditDto userEditDto){
        userService.update(id, userEditDto);
        return "redirect:/admin/users/" + id;
    }
    @PostMapping("/{id}/delete_role/{role_id}")
    public String clearUserRoles(@PathVariable Long id, @PathVariable Long role_id){
        userService.clearUserRoles(id, role_id);
        return "redirect:/admin/users/" + id;
    }
    @PostMapping("/{id}/delete_user")
    public String deleteUserById(@PathVariable Long id){
        userService.deleteUserById(id);
        return "redirect:/admin/users";
    }
}

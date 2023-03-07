package com.example.sweater_letscode.spring.controller;

import com.example.sweater_letscode.spring.dto.RoleReadDto;
import com.example.sweater_letscode.spring.dto.UserEditDto;
import com.example.sweater_letscode.spring.dto.UserReadDto;
import com.example.sweater_letscode.spring.filter.PageResponse;
import com.example.sweater_letscode.spring.filter.UserFilter;
import com.example.sweater_letscode.spring.service.RoleService;
import com.example.sweater_letscode.spring.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/users")
@RequiredArgsConstructor
@Slf4j
public class AdminController {
    private final UserService userService;
    private final RoleService roleService;
//    @ExceptionHandler
//    public String handleException(Exception exception, HttpServletRequest httpServletRequest){
//        log.error("Failed to return response", exception);
//        return "500";
//    }

//    @GetMapping
//    public String showAllUsers(Model model) {
//        model.addAttribute("users", userService.showAllUsers());
//        return "admin/users";
//    }
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
        model.addAttribute("seeMessages", "false");
        model.addAttribute("roles", roleService.findAll());
        model.addAttribute("userICheck", userService.findById(id).get());
        model.addAttribute("user", userService.findByUsername(userService.usernameFromContext()).get());
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

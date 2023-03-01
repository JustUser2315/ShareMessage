package com.example.sweater_letscode.spring.mapper;

import com.example.sweater_letscode.spring.dto.RoleEditDto;
import com.example.sweater_letscode.spring.dto.UserEditDto;
import com.example.sweater_letscode.spring.entity.Role;
import com.example.sweater_letscode.spring.entity.User;
import com.example.sweater_letscode.spring.repository.RoleRepository;
import com.example.sweater_letscode.spring.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserEditToEntityMapper implements MyCustomMapper<UserEditDto, User> {
    private final RoleEntityToReadMapper roleEntityToReadMapper;
    private final RoleRepository roleRepository;
    private final ImageService imageService;


    public User mapForUpdate(UserEditDto userEditDto, User user) {
// set empty Set of RoleEditDto for exception avoiding
        if (userEditDto.getRoles() == null) {
            userEditDto.setRoles(Collections.emptySet());
        }


        // giving roles from UserEditDto and finding them in database by name. If find -> set this role to user. As we're using Set, it helps us to avoid collisions
        var allRolesName = userEditDto.getRoles().stream().map(RoleEditDto::getName).toList();
        var allByNameIn = roleRepository.findAllByNameIn(allRolesName);
        for (Role r : allByNameIn) {
            user.getRoles().add(r);
        }

        // if USerEditDto doesn't have a username, then we take an existing one from user
        if (userEditDto.getUsername() == null) {
            userEditDto.setUsername(user.getUsername());
        }
        // if USerEditDto doesn't have an email, then we take an existing one from user
        if (userEditDto.getEmail() == null) {
            userEditDto.setEmail(user.getEmail());
        }
        // in case, when we change email, we need to substitute user active status 'cause email was changed and user must confirm it by new activation code
        if (!userEditDto.getEmail().equals(user.getEmail())) {
            user.setActive(false);
            user.setActivationCode(UUID.randomUUID().toString());
            userEditDto.setActivationCode(user.getActivationCode());
        }

        // process of avatar changing
        if (userEditDto.getAvatar() != null) {
            try {
                imageService.uploadAvatar(userEditDto.getAvatar().getOriginalFilename(), userEditDto.getAvatar().getInputStream());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            user.setAvatar(userEditDto.getAvatar().getOriginalFilename());
        }


        if(user.getSubscribers()==null){
            user.setSubscribers(Collections.emptySet());
        }
        if(user.getSubscriptions()==null){
            user.setSubscriptions(Collections.emptySet());
        }
//        if(userEditDto.getSubscribers()==null){
//            userEditDto.setSubscribers(Collections.emptySet());
//        }
//        if(userEditDto.getSubscriptions()==null){
//            userEditDto.setSubscriptions(Collections.emptySet());
//        }


        return User.builder()
                .id(user.getId())
                .password(user.getPassword())
                .username(userEditDto.getUsername())
                .active(user.isActive())
                .roles(user.getRoles())
                .email(userEditDto.getEmail())
                .activationCode(user.getActivationCode())
                .subscribers(user.getSubscribers())
                .subscriptions(user.getSubscriptions())
                .build();


    }

    @Override
    public User map(UserEditDto userEditDto) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

//        if(userEditDto.getSubscribers()==null){
//            userEditDto.setSubscribers(Collections.emptySet());
//        }
//        if(userEditDto.getSubscriptions()==null){
//            userEditDto.setSubscriptions(Collections.emptySet());
//        }
        /*
         * initial user are created with UserEditDtoData(username, password, email)
         * without roles, avatar, and with random generated activation code */
        var user = User.builder()
                .username(userEditDto.getUsername())
                .password(passwordEncoder.encode(userEditDto.getPassword()))
                .active(false)
                .email(userEditDto.getEmail())
                .subscriptions(Collections.emptySet())
                .subscribers(Collections.emptySet())
                .build();

        // assign role for user. If role doesn't exist, we just add it to DB
        var role_user = roleRepository.findRoleByName("ROLE_USER");
        if (role_user.isPresent()) {
            user.setRoles(Set.of(role_user.get()));
        } else {
            roleRepository.save(Role.builder().name("ROLE_USER").build());
            user.setRoles(Set.of(roleRepository.findRoleByName("ROLE_USER").get()));
        }

        // check UserEditDto avatar existing. And if it does, we set name of file to user
        if (!userEditDto.getAvatar().isEmpty()) {
            try {
                imageService.uploadAvatar(userEditDto.getAvatar().getOriginalFilename(), userEditDto.getAvatar().getInputStream());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            user.setAvatar(userEditDto.getAvatar().getOriginalFilename());
        }else {
            user.setAvatar("default_avatar.png");
        }

        // assign activation code for user through UserEditDto 'cause we use this class for sending email for account activation confirming
        userEditDto.setActivationCode(UUID.randomUUID().toString());
        user.setActivationCode(userEditDto.getActivationCode());


        return user;

    }
}

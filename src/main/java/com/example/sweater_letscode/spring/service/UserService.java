package com.example.sweater_letscode.spring.service;

import com.example.sweater_letscode.spring.dto.RoleReadDto;
import com.example.sweater_letscode.spring.dto.UserEditDto;
import com.example.sweater_letscode.spring.dto.UserReadDto;
import com.example.sweater_letscode.spring.entity.Role;
import com.example.sweater_letscode.spring.entity.User;
import com.example.sweater_letscode.spring.mapper.UserEditToEntityMapper;
import com.example.sweater_letscode.spring.mapper.UserEntityToReadMapper;
import com.example.sweater_letscode.spring.repository.RoleRepository;
import com.example.sweater_letscode.spring.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final UserEditToEntityMapper userEditToEntityMapper;
    private final UserEntityToReadMapper userEntityToReadMapper;
    private final RoleRepository roleRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var maybeUser = userRepository.findByUsername(username);
        if(!maybeUser.isPresent()){
            throw new UsernameNotFoundException("User not found");
        }
        return maybeUser.get();
    }
   @Transactional
    public UserReadDto registration(UserEditDto userEditDto){
        var savedUser = userRepository.save(userEditToEntityMapper.map(userEditDto));
       return userEntityToReadMapper.map(savedUser);
    }
}

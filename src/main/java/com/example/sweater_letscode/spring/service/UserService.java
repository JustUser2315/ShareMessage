package com.example.sweater_letscode.spring.service;

import com.example.sweater_letscode.spring.dto.UserEditDto;
import com.example.sweater_letscode.spring.dto.UserFullReadDto;
import com.example.sweater_letscode.spring.dto.UserReadDto;
import com.example.sweater_letscode.spring.entity.QUser;
import com.example.sweater_letscode.spring.filter.UserFilter;
import com.example.sweater_letscode.spring.mapper.RoleReadToEntityMapper;
import com.example.sweater_letscode.spring.mapper.UserEditToEntityMapper;
import com.example.sweater_letscode.spring.mapper.UserEntityToFullREadMapper;
import com.example.sweater_letscode.spring.mapper.UserEntityToReadMapper;
import com.example.sweater_letscode.spring.querydsl.QPredicates;
import com.example.sweater_letscode.spring.repository.RoleRepository;
import com.example.sweater_letscode.spring.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final UserEditToEntityMapper userEditToEntityMapper;
    private final UserEntityToReadMapper userEntityToReadMapper;
    private final UserEntityToFullREadMapper userEntityToFullREadMapper;
    private final RoleReadToEntityMapper roleReadToEntityMapper;
    private final RoleRepository roleRepository;

    public Page<UserFullReadDto> showAllUsersWithFilter(UserFilter userFilter, Pageable pageable) {
        userFilter.setActive(true);
        userFilter.setRoles(Collections.emptySet());


        var build = QPredicates.builder()
                .add(userFilter.getUsername(), QUser.user.username::containsIgnoreCase)
                .add(userFilter.isActive(), QUser.user.active::eq).build();

        if (build != null) {
            return userRepository.findAll(build, pageable)
                    .map(userEntityToFullREadMapper::map);
        } else {
            return userRepository.findAll(pageable)
                    .map(userEntityToFullREadMapper::map);
        }

    }

    public List<UserFullReadDto> showAllUsers() {
        return userRepository.findAll()
                .stream().map(userEntityToFullREadMapper::map)
                .toList();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var maybeUser = userRepository.findByUsername(username);
        if (!maybeUser.isPresent()) {
            throw new UsernameNotFoundException("User not found");
        }
        return maybeUser.get();
    }

    @Transactional
    public UserReadDto registration(UserEditDto userEditDto) {
        var savedUser = userRepository.save(userEditToEntityMapper.map(userEditDto));
        return userEntityToReadMapper.map(savedUser);
    }


    public Optional<UserFullReadDto> findById(Long id) {
        var byId = userRepository.findById(id);
        var user = byId.orElseThrow(() -> new UsernameNotFoundException("User with id = " + id + "doesn't exist"));
        return Optional.ofNullable(userEntityToFullREadMapper.map(user));
    }

    @Transactional
    public Optional<UserFullReadDto> update(Long id, UserEditDto userEditDto) {
        return userRepository.findById(id)
                .map(user -> userEditToEntityMapper.mapForUpdate(userEditDto, user))
                .map(userRepository::saveAndFlush)
                .map(userEntityToFullREadMapper::map);
    }

    @Transactional
    public void clearUserRoles(Long userId, Long roleId) {
        roleRepository.clearUserRoles(userId, roleId);
        roleRepository.flush();

    }

    @Transactional
    public boolean deleteUserById(Long userId) {
        userRepository.deleteById(userId);
        userRepository.flush();
        return userRepository.findById(userId).isEmpty();
    }

    public Optional<UserFullReadDto> findByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(userEntityToFullREadMapper::map);
    }

    @Transactional
    public void updatePassword(Long id, String newPassword) {
        newPassword = new BCryptPasswordEncoder().encode(newPassword);
        userRepository.updatePassword(id, newPassword);
    }


    public String usernameFromContext() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}

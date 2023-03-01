package com.example.sweater_letscode.spring.service;

import com.example.sweater_letscode.spring.dto.UserEditDto;
import com.example.sweater_letscode.spring.dto.UserReadDto;
import com.example.sweater_letscode.spring.entity.QUser;
import com.example.sweater_letscode.spring.entity.User;
import com.example.sweater_letscode.spring.filter.UserFilter;
import com.example.sweater_letscode.spring.mapper.UserEditToEntityMapper;
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

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final UserEditToEntityMapper userEditToEntityMapper;
    private final UserEntityToReadMapper userEntityToReadMapper;
    private final RoleRepository roleRepository;
    private final EmailServiceImpl emailService;
    private final ImageService imageService;

    public Page<UserReadDto> showAllUsersWithFilter(UserFilter userFilter, Pageable pageable) {
        userFilter.setRoles(Collections.emptySet());


        var build = QPredicates.builder()
                .add(userFilter.getEmail(), QUser.user.email::containsIgnoreCase)
                .add(userFilter.getUsername(), QUser.user.username::containsIgnoreCase)
                .add(userFilter.getActive(), QUser.user.active.stringValue()::containsIgnoreCase)
                .build();


        if (build != null) {
            return userRepository.findAll(build, pageable)
                    .map(userEntityToReadMapper::map);
        } else {
            return userRepository.findAll(pageable)
                    .map(userEntityToReadMapper::map);
        }

    }

//    public List<UserReadDto> showAllUsers() {
//        return userRepository.findAll()
//                .stream().map(userEntityToReadMapper::map)
//                .toList();
//    }

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
        sendActivationCode(userEditDto);

        return userEntityToReadMapper.map(savedUser);
    }

    public void sendActivationCode(UserEditDto userEditDto) {
        if (!userEditDto.getEmail().isEmpty()) {
            emailService.send(userEditDto.getEmail(), "Activation code", """
                    Hello, %s! Welcome to sweater app! Please, click on link to activate your account
                    http://localhost:8080/user/activate/%s
                    """.formatted(userEditDto.getUsername(), userEditDto.getActivationCode()));
        }
    }

    @Transactional
    public boolean doesUserHaveAnActivationCode(String activationCode) {
//        var mbUser = userRepository.findByActivationCode(activationCode).get();
//        if(mbUser != null){
//            mbUser.setActive(true);
//            mbUser.setActivationCode(null);
//            userRepository.saveAndFlush(mbUser);
//            return true;
//        }
//        return false;

        var mbUser = userRepository.findByActivationCode(activationCode);
        if (mbUser.isPresent()) {
            User user = mbUser.get();
            user.setActive(true);
            user.setActivationCode(null);
            userRepository.saveAndFlush(user);
            return true;
        }
        return false;
    }


    public Optional<UserReadDto> findById(Long id) {
        var byId = userRepository.findById(id);
        var user = byId.orElseThrow(() -> new UsernameNotFoundException("User with id = " + id + "doesn't exist"));
        return Optional.ofNullable(userEntityToReadMapper.map(user));
    }

    @Transactional
    public Optional<UserReadDto> update(Long id, UserEditDto userEditDto) {
        var retData = userRepository.findById(id)
                .map(user -> userEditToEntityMapper.mapForUpdate(userEditDto, user))
                .map(userRepository::saveAndFlush)
                .map(userEntityToReadMapper::map);
        sendActivationCode(userEditDto);
        return retData;
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

    public Optional<UserReadDto> findByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(userEntityToReadMapper::map);
    }

    @Transactional
    public void updatePassword(Long id, String newPassword) {
        newPassword = new BCryptPasswordEncoder().encode(newPassword);
        userRepository.updatePassword(id, newPassword);
    }

    public Optional<byte[]> findAvatar(Long id) throws IOException {
        var mbUser = userRepository.findById(id);
        if (!mbUser.isPresent()) {
            return Optional.empty();
        }
        return imageService.loadAvatar(mbUser.get().getAvatar());
    }

    @Transactional
    public void subscribe(Long userId, Long whoFollowId) {
        userRepository.subscribe(userId, whoFollowId);
    }

    @Transactional
    public void unsubscribe(Long userId, Long whoFollowId) {
        userRepository.unsubscribe(userId, whoFollowId);
    }

    public boolean isAlreadySub(Long userId, Long whoFollowId) {
        Long l = userRepository.isSub(userId, whoFollowId);
        return l>0;
    }


    public String usernameFromContext() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}

package com.project.share_message.spring.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserReadDto {
    private Long id;
    private String email;
    private String username;
    private boolean active;
    private String activationCode;
    private String avatar;
    private Set<MessageReadDto> messages;
    private Set<RoleReadDto> roles;
    private Set<UserReadDto> subscriptions;
    private Set<UserReadDto> subscribers;
    private Set<MessageReadDto> likes;
}

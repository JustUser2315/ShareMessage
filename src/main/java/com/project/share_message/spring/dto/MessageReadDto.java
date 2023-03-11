package com.project.share_message.spring.dto;

import com.project.share_message.spring.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageReadDto {
    private Integer id;
    private String text;
    private String tag;
    private String authorUsername;
    private Long authorId;
    private String picture;
    private Set<User> likes;

}

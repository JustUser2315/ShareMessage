package com.example.sweater_letscode.spring.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

}

package com.example.sweater_letscode.spring.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageEditDto {
    @Size(min = 3, max = 2000, message = "The message must contain 3-2000 characters")
    @NotBlank(message = "The message mustn't be blank")
    private String text;
    @Size(min = 3, max = 25, message = "The tag must contain 3-25 characters")
    private String tag;
}

package com.example.sweater_letscode.spring.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageEditDto {
    @Size(min = 3, max = 2000, message = "Sorry, the text you entered does not meet the required format. The message must contain at least 3 characters. Please try again with a valid text entry that meets these requirements.")
    @NotBlank(message = "Sorry, the text you entered does not meet the required format. The text is  blank or empty. Please try again with a valid text entry that meets these requirements.")
//    @NotEmpty(message = "Sorry, the text you entered does not meet the required format. The text is  blank or empty. Please try again with a valid text entry that meets these requirements.")
    private String text;
    @Size(min = 3, max = 25, message = "Sorry, the text you entered does not meet the required format. The message must contain at least 3 characters. Please try again with a valid text entry that meets these requirements.")
    @NotBlank(message = "Sorry, the text you entered does not meet the required format. The text is  blank or empty. Please try again with a valid text entry that meets these requirements.")
//    @NotEmpty(message = "Sorry, the text you entered does not meet the required format. The text is  blank or empty. Please try again with a valid text entry that meets these requirements.")
    private String tag;

    private MultipartFile picture;
}

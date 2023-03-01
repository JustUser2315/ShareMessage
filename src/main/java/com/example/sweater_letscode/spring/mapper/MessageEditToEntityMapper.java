package com.example.sweater_letscode.spring.mapper;

import com.example.sweater_letscode.spring.dto.MessageEditDto;
import com.example.sweater_letscode.spring.entity.Message;
import com.example.sweater_letscode.spring.entity.User;
import com.example.sweater_letscode.spring.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class MessageEditToEntityMapper implements MyCustomMapper<MessageEditDto, Message>{
    private final ImageService imageService;
    @Override
    public Message map(MessageEditDto messageEditDto) {
        // take a user from security context. If it doesn't exist, we assign "unknown" user.
        User user = User.builder()
                .username("unknown").build();
        //TODO: we can delete checking 'cause SecurityFilterChain shouldn't give access to message creating for users without name...
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null){
            user = (User) authentication.getPrincipal();
        }


        Message message = Message.builder()
                .text(messageEditDto.getText())
                .tag(messageEditDto.getTag())
                .author(user)
                .build();
        if(!messageEditDto.getPicture().isEmpty()){
            try {
                imageService.uploadMessagePicture(messageEditDto.getPicture().getOriginalFilename(), messageEditDto.getPicture().getInputStream());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            message.setPicture(messageEditDto.getPicture().getOriginalFilename());
        }

        return message;
    }

    public Message mapForUpdate(MessageEditDto messageEditDto, Message message){
        if(messageEditDto.getText()!=null){
            message.setText(messageEditDto.getText());
        }
        if(messageEditDto.getTag()!=null){
            message.setTag(messageEditDto.getTag());
        }
        if(!messageEditDto.getPicture().isEmpty()){
            try {
                imageService.uploadMessagePicture(messageEditDto.getPicture().getOriginalFilename(), messageEditDto.getPicture().getInputStream());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            message.setPicture(messageEditDto.getPicture().getOriginalFilename());
        }


        Message updatedMessage = Message.builder()
                .id(message.getId())
                .text(message.getText())
                .tag(message.getTag())
                .author(message.getAuthor())
                .picture(message.getPicture())
                .build();
        return updatedMessage;
    }
}

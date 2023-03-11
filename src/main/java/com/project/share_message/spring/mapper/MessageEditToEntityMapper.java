package com.project.share_message.spring.mapper;

import com.project.share_message.spring.dto.MessageEditDto;
import com.project.share_message.spring.entity.Message;
import com.project.share_message.spring.entity.User;
import com.project.share_message.spring.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;

@Component
@RequiredArgsConstructor
public class MessageEditToEntityMapper implements MyCustomMapper<MessageEditDto, Message>{
    private final ImageService imageService;
    @Override
    public Message map(MessageEditDto messageEditDto) {
        // take a userICheck from security context. If it doesn't exist, we assign "unknown" userICheck.
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
                .likes(Collections.emptySet())
                .build();

        if(messageEditDto.getPicture()!=null){
            if(!messageEditDto.getPicture().isEmpty()){
                try {
                    imageService.uploadMessagePicture(messageEditDto.getPicture().getOriginalFilename(), messageEditDto.getPicture().getInputStream());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                message.setPicture(messageEditDto.getPicture().getOriginalFilename());
            }
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
        if(messageEditDto.getPicture()!=null){
            if(!messageEditDto.getPicture().isEmpty()){
                try {
                    imageService.uploadMessagePicture(messageEditDto.getPicture().getOriginalFilename(), messageEditDto.getPicture().getInputStream());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                message.setPicture(messageEditDto.getPicture().getOriginalFilename());
            }
        }else {
            message.setPicture(message.getPicture());
        }


        Message updatedMessage = Message.builder()
                .id(message.getId())
                .text(message.getText())
                .tag(message.getTag())
                .author(message.getAuthor())
                .picture(message.getPicture())
                .likes(message.getLikes())
                .build();
        return updatedMessage;
    }
}

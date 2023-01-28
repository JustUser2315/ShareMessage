package com.example.sweater_letscode.spring.mapper;

import com.example.sweater_letscode.spring.dto.MessageEditDto;
import com.example.sweater_letscode.spring.entity.Message;
import com.example.sweater_letscode.spring.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class MessageEditToEntityMapper implements MyCustomMapper<MessageEditDto, Message>{
    @Override
    public Message map(MessageEditDto messageEditDto) {
        User user = User.builder()
                .username("unknown").build();
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null){
            user = (User) authentication.getPrincipal();
        }
        return  Message.builder()
                .text(messageEditDto.getText())
                .tag(messageEditDto.getTag())
                .author(user)
                .build();
    }
}

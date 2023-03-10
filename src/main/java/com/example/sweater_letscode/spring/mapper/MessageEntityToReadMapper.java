package com.example.sweater_letscode.spring.mapper;

import com.example.sweater_letscode.spring.dto.MessageReadDto;
import com.example.sweater_letscode.spring.entity.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageEntityToReadMapper implements MyCustomMapper<Message, MessageReadDto> {
    UserEntityToReadMapper userEntityToReadMapper;
    @Override
    public MessageReadDto map(Message message) {

        return MessageReadDto.builder()
                .id(message.getId())
                .text(message.getText())
                .tag(message.getTag())
                .authorUsername(message.getAuthor().getUsername())
                .authorId(message.getAuthor().getId())
                .picture(message.getPicture())
                .likes(message.getLikes())
                .build();
    }
}

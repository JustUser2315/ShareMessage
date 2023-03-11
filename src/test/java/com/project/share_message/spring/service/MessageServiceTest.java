package com.project.share_message.spring.service;

import com.project.share_message.spring.TestBaseApplication;
import com.project.share_message.spring.dto.MessageEditDto;
import com.project.share_message.spring.dto.MessageReadDto;
import com.project.share_message.spring.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestConstructor;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@RequiredArgsConstructor
@ExtendWith(MockitoExtension.class)
class MessageServiceTest extends TestBaseApplication {

    private final MessageService messageService;
    private final JdbcTemplate jdbcTemplate;
    private final MessageRepository messageRepository;

    @Test
    void findAllByAuthorId() {
        var expectedResult = messageService.findAllByAuthorId(1L);
        MessageReadDto m1 = new MessageReadDto(1, "text1", "tag1", "username1", 1L, null, Collections.emptySet());
        MessageReadDto m2 = new MessageReadDto(2, "text2", "tag2", "username1", 1L, null, Collections.emptySet());
        MessageReadDto m3 = new MessageReadDto(3, "text3", "tag3", "username1", 1L, null, Collections.emptySet());
        assertThat(expectedResult).hasSize(3);
        Assertions.assertEquals(expectedResult.get(0), m1);
        Assertions.assertEquals(expectedResult.get(1), m2);
        Assertions.assertEquals(expectedResult.get(2), m3);


    }

    @Test
    void delete() {
        Assertions.assertTrue(messageRepository.findById(1).isPresent());
        messageService.delete(1);
        Assertions.assertTrue(messageRepository.findById(1).isEmpty());



    }

    @Test
    void save() {
        MessageEditDto messageEditDto = new MessageEditDto("TestText", "TestTag", null);
        var expectedResult = messageService.save(messageEditDto);
        Assertions.assertEquals(messageEditDto.getText(), expectedResult.getText());
        Assertions.assertEquals(messageEditDto.getTag(), expectedResult.getTag());
    }

}
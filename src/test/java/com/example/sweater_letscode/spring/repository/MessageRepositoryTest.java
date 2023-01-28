package com.example.sweater_letscode.spring.repository;

import com.example.sweater_letscode.spring.TestBaseApplication;
import com.example.sweater_letscode.spring.dto.MessageEditDto;
import com.example.sweater_letscode.spring.entity.Message;
import com.example.sweater_letscode.spring.entity.Role;
import com.example.sweater_letscode.spring.entity.User;
import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.Extension;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@RequiredArgsConstructor
@ExtendWith(MockitoExtension.class)
class MessageRepositoryTest extends TestBaseApplication {
    private final MessageRepository messageRepository;

    @Test
    void findAll() {
        var expectResult = messageRepository.findAll();
        Assertions.assertThat(expectResult).hasSize(3);
    }

    @Test
    void findFirst() {
        var expectResult = messageRepository.findById(2);
//        Assertions.assertThat(expectResult).isPresent();
        assertTrue(expectResult.isPresent());
        var expectText = expectResult.get().getText();
        Assertions.assertThat(expectText).isEqualTo("text2");
    }

    @Test
    void save() {
        var user = User.builder()
                .id(1L)
                .build();
        Message message = Message.builder()
                .text("TestText")
                .tag("TestTag")
                .author(user)
                .build();
        var savedMessage = messageRepository.save(message);
        assertEquals(message.getText(), savedMessage.getText());
        assertEquals(message.getTag(), savedMessage.getTag());
        assertEquals(message.getAuthor(), savedMessage.getAuthor());
        assertEquals(savedMessage.getId(), 4);

    }

    @Test
    void delete() {

        var expectedMessage = messageRepository.findById(3);
        expectedMessage.ifPresent(messageRepository::delete);
        var expectedResult = messageRepository.findAll();
        Assertions.assertThat(expectedResult).hasSize(2);

    }

}
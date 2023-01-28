package com.example.sweater_letscode.spring.service;

import com.example.sweater_letscode.spring.TestBaseApplication;
import com.example.sweater_letscode.spring.dto.MessageEditDto;
import com.example.sweater_letscode.spring.entity.Message;
import com.example.sweater_letscode.spring.filter.MessageFilter;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.provider.Arguments;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.stereotype.Component;
import org.springframework.test.context.TestConstructor;

import java.util.stream.Stream;

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@RequiredArgsConstructor
@ExtendWith(MockitoExtension.class)
class MessageServiceTest extends TestBaseApplication {

    private final MessageService messageService;



    @Test
    void save() {
        MessageEditDto messageEditDto =  new MessageEditDto("TestText", "TestTag");
        var expectedResult = messageService.save(messageEditDto);
        Assertions.assertEquals(messageEditDto.getText(), expectedResult.getText());
        Assertions.assertEquals(messageEditDto.getTag(), expectedResult.getTag());
    }

}
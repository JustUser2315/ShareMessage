package com.example.sweater_letscode.spring;

import com.example.sweater_letscode.spring.repository.MessageRepository;
import com.example.sweater_letscode.spring.repository.UserRepository;
import com.example.sweater_letscode.spring.service.MessageService;
import com.example.sweater_letscode.spring.service.UserService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@RequiredArgsConstructor
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@ExtendWith(MockitoExtension.class)
@SpringBootTest
@Transactional
public class CheckTester{
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    private final MessageService messageService;
    private final UserService userService;
    @Test

    @Disabled
    void check() throws IOException {
        Optional<byte[]> avatar = userService.findAvatar(2L);
        Path path = Paths.get("C:\\Users\\vende\\OneDrive\\Desktop\\TESTSTST\\");
        Path write = Files.write(path, avatar.get());
        System.out.println(avatar.get());


    }
}

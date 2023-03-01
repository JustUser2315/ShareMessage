package com.example.sweater_letscode.spring.repository;

import com.example.sweater_letscode.spring.TestBaseApplication;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.TestConstructor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@RequiredArgsConstructor
@ExtendWith(MockitoExtension.class)
class UserRepositoryTest extends TestBaseApplication {
private final UserRepository userRepository;

@Test
void findByActivationCode(){
    var expResult = userRepository.findByActivationCode("activation-code-example");
    Assertions.assertTrue(expResult.isPresent());
    Assertions.assertEquals(expResult.get(), userRepository.findById(1L).get());
}

    @Test
    void findByUsername() {
        var expectedResult = userRepository.findByUsername("username1");
        Assertions.assertTrue(expectedResult.isPresent());
        expectedResult.ifPresent(user-> {
            assertEquals(1L, user.getId());
            assertEquals("password1", user.getPassword());
            assertFalse(user.isActive());
        });
    }

    @Test
    void findById(){
        var mbUser = userRepository.findById(1L);
        assertEquals(mbUser.get().getUsername(),"username1");
        assertEquals(mbUser.get().getPassword(),"password1");
        assertEquals(mbUser.get().getId(),1L);
    }

    @Test
    void findAll(){
        var expectedResult = userRepository.findAll();
         assertThat(expectedResult).hasSize(2);
    }

    @Test
    void updatePassword(){
        userRepository.updatePassword(1L, "newPasswordForUser2");
        var byId = userRepository.findById(1L);
        assertTrue(byId.isPresent());
        assertEquals(byId.get().getPassword(), "newPasswordForUser2");
    }

}
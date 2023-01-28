package com.example.sweater_letscode.spring;

import com.example.sweater_letscode.spring.entity.User;
import com.example.sweater_letscode.spring.repository.UserRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication
public class SweaterLetsCodeApplication {

    public static void main(String[] args) {
        SpringApplication.run(SweaterLetsCodeApplication.class, args);
    }

}

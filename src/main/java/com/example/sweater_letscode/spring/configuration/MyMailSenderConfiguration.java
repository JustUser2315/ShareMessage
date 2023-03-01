package com.example.sweater_letscode.spring.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

@Configuration
@PropertySource("classpath:spring.properties")
public class MyMailSenderConfiguration {

    @Value("${spring.mail.host}")
    private  String host;
    @Value("${spring.mail.port}")
    private  int port;
    private   String username = mail_sender_password_and_username().getProperty("spring.mail.username");

    private  String password = mail_sender_password_and_username().getProperty("spring.mail.password");


    @Bean
    public JavaMailSender mailSender(){
    JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
    javaMailSender.setHost(host);
    javaMailSender.setPort(port);
    javaMailSender.setUsername(username);
    javaMailSender.setPassword(password);

        var javaMailProperties = javaMailSender.getJavaMailProperties();
        javaMailProperties.put("mail.transport.protocol", "smtp");
        javaMailProperties.put("mail.smtp.auth", "true");
        javaMailProperties.put("mail.smtp.starttls.enable", "true");
        javaMailProperties.put("mail.debug", "true");

        return javaMailSender;

    }
        private Properties mail_sender_password_and_username () {
            Properties prop = new Properties();
            try {
                prop.load(new FileInputStream("C:\\Java_Kit\\Java_WorkSpace\\AppsData\\mail_sender_password_and_username.properties"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return  prop;
        }


}

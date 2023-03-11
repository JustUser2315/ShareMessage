package com.project.share_message.spring.configuration;

import com.project.share_message.spring.recaptcha.RecaptchaAuthenticationFailureHandler;
import com.project.share_message.spring.recaptcha.RecaptchaFilter;
import com.project.share_message.spring.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class MySecurityConfiguration {
    private final UserService userService;
    private final RecaptchaAuthenticationFailureHandler authenticationFailureHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests(auth-> auth
                        .antMatchers("/", "/registration", "/login", "/registration", "/user/activate/*", "/main", "/user/*/profile/avatar", "/messages/*/picture", "/main/logo").permitAll()
                        .antMatchers("/messages").hasAnyRole("USER", "ADMIN")
                        .antMatchers("/admin/**").hasRole("ADMIN")
                        .antMatchers("/user/**").hasAnyRole("USER", "ADMIN")
                        .anyRequest().authenticated())
                .formLogin(login->login
                        .loginPage("/login")
                        .failureHandler(authenticationFailureHandler)
                        .defaultSuccessUrl("/messages")
                        .permitAll())
                .logout(logout->logout.logoutUrl("/logout")
                        .logoutSuccessUrl("/main"))
                .addFilterBefore(new RecaptchaFilter(authenticationFailureHandler), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling()
                .accessDeniedPage("/403");;

        return http.build();
    }

    @Bean(name = "getPasswordEncoder")
    public PasswordEncoder getPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        var authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userService)
                .passwordEncoder(getPasswordEncoder());
        return authenticationManagerBuilder.build();
    }




}

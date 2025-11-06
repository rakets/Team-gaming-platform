package com.project.gamingplatform.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
//                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
//                .csrf(csrf -> csrf.disable());
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/users/register", "/users/registerForm", "/css/**", "/js/**").permitAll() // доступно всем
                        .anyRequest().authenticated() // всё остальное требует логина
                )
                .formLogin(form -> form
                        .loginPage("/login") // можешь сделать свою страницу логина
                        .permitAll()
                )
                .logout(logout -> logout.permitAll());
        return http.build();
    }
}

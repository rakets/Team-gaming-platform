package com.project.gamingplatform.config;

import com.project.gamingplatform.service.CustomUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebSecurityConfig {
    private final String[] publicUrl = {"/",
            "/login", "/register", "/register/**", "/webjars/**", "/resources/**", "/assets/**",
            "/css/**", "/summernote/**", "/js/**", "/*.css", "/*.js", "/*.js.map", "/fonts**",
            "/favicon.ico", "/resources/**", "/error"};
    private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
    private final CustomUserDetailService customUserDetailService;

    public WebSecurityConfig(CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler, CustomUserDetailService customUserDetailService) {
        this.customAuthenticationSuccessHandler = customAuthenticationSuccessHandler;
        this.customUserDetailService = customUserDetailService;
    }

    @Bean
    protected SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
//        httpSecurity.authenticationProvider(authenticationProvider());

        httpSecurity
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers(publicUrl).permitAll();
                    auth.anyRequest().authenticated();
                });
        httpSecurity.formLogin(login -> login
                        .loginPage("/login").permitAll()
                        .successHandler(customAuthenticationSuccessHandler)) // !!!!!!!
                .logout(logout -> logout
                        .logoutUrl("/logout")
                    .logoutSuccessUrl("/")
                ).cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable());
        return httpSecurity.build();
    }

    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(customUserDetailService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

//    @Bean
//    protected SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
//        httpSecurity
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers(publicUrl).permitAll()
//                        .anyRequest().authenticated()
//                )
//                .formLogin(form -> form
//                        .loginPage("/login")
//                        .permitAll()
//                        .defaultSuccessUrl("/dashboard", true)
//                )
//                .logout(logout -> logout
//                        .logoutUrl("/logout")
//                        .logoutSuccessUrl("/")
//                        .permitAll()
//                )

    /// /                .csrf(csrf -> csrf.disable());
//                .csrf(Customizer.withDefaults()); //!!!!!! что это
//
//        return httpSecurity.build();
//    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

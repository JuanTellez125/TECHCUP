package edu.dosw.TECHCUP.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

//Tiene su porpia @Configuration puede resolver el bean sin el SecurityConfig
@Configuration
public class PasswordConfig {

    //Es para cuando se quiera encriptar una contraseña
    //asi no depende de otras y sabe por donde empezar sin dar error
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

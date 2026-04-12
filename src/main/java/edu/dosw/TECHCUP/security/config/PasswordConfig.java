package edu.dosw.TECHCUP.security.config;

import edu.dosw.TECHCUP.core.util.Base64PasswordEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
public class PasswordConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new Base64PasswordEncoder();
    }
}

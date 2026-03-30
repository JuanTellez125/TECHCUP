package edu.dosw.TECHCUP.security.oauth2.config;


import edu.dosw.TECHCUP.security.oauth2.service.OAuth2UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class OAuth2SecurityConfig {

    private final OAuth2UserService oAuth2UserService;

    public OAuth2SecurityConfig(OAuth2UserService customOAuth2UserService) {
        this.oAuth2UserService = customOAuth2UserService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/oauth2/public").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(info ->
                                info.userService(oAuth2UserService))
                        .defaultSuccessUrl("/api/oauth2/profile", true)
                        .failureUrl("/api/oauth2/public?error=true")
                );
        return http.build();
    }
}

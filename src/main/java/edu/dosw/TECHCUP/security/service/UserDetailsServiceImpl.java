package edu.dosw.TECHCUP.security.service;

import edu.dosw.TECHCUP.core.model.User;
import edu.dosw.TECHCUP.core.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    @Qualifier("playerService")
    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.debug("Cargando usuario por email: {}", email);

        User user = userService.findByEmail(email)
                .orElseThrow(() -> {
                    log.warn("Usuario no encontrado para autenticacion: {}", email);
                    return new UsernameNotFoundException(
                            "Usuario no encontrado con email: " + email);
                });

        // Carga el rol del usuario: PLAYER, CAPTAIN, REFEREE, ADMINISTRATOR, ORGANIZER
        String roleAuthority = "ROLE_" + user.getUserType().name();
        log.debug("Usuario {} autenticado con rol: {}", email, roleAuthority);

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .authorities(List.of(new SimpleGrantedAuthority(roleAuthority)))
                .build();
    }
}
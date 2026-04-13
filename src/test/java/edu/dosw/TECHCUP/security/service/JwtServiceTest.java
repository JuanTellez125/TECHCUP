package edu.dosw.TECHCUP.security.service;

import edu.dosw.TECHCUP.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

    @InjectMocks
    private JwtService jwtService;

    private static final String SECRET =
            "dGVjaGN1cC1zZWNyZXQta2V5LXBhcmEtand0LWF1dGhlbnRpY2F0aW9uLTI1NmJpdHM=";
    private static final long EXPIRATION_MS = 3_600_000L; // 1 hora

    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(jwtService, "secretKey", SECRET);
        ReflectionTestUtils.setField(jwtService, "expirationMs", EXPIRATION_MS);

        userDetails = User.builder()
                .username("test@techcup.com")
                .password("password")
                .authorities(Collections.emptyList())
                .build();
    }

    // ─── Flujo correcto ────────────────────────────────────────────────────────

    @Test
    @DisplayName("Flujo correcto: token generado es válido para el usuario")
    void shouldGenerateValidToken() {
        String token = jwtService.generateToken(userDetails);

        assertNotNull(token);
        assertTrue(jwtService.isTokenValid(token, userDetails));
    }

    @Test
    @DisplayName("Flujo correcto: se extrae el username del token correctamente")
    void shouldExtractUsernameFromToken() {
        String token = jwtService.generateToken(userDetails);

        String username = jwtService.extractUsername(token);

        assertEquals("test@techcup.com", username);
    }

    @Test
    @DisplayName("Flujo correcto: el token no está expirado al ser generado")
    void shouldNotBeExpiredOnCreation() {
        String token = jwtService.generateToken(userDetails);

        Date expiration = jwtService.extractExpiration(token);

        assertTrue(expiration.after(new Date()));
    }

    // ─── Flujo token expirado ──────────────────────────────────────────────────

    @Test
    @DisplayName("Flujo token expirado: isTokenValid retorna false")
    void shouldReturnFalseForExpiredToken() {
        ReflectionTestUtils.setField(jwtService, "expirationMs", -1L);
        String expiredToken = jwtService.generateToken(userDetails);

        boolean valid = jwtService.isTokenValid(expiredToken, userDetails);

        assertFalse(valid);
    }

    // ─── Flujo token inválido ──────────────────────────────────────────────────

    @Test
    @DisplayName("Flujo token inválido: token con firma alterada retorna false")
    void shouldReturnFalseForTamperedToken() {
        String token = jwtService.generateToken(userDetails);
        String[] parts = token.split("\\.");
        String tamperedToken = parts[0] + ".INVALIDO." + parts[2];

        boolean valid = jwtService.isTokenValid(tamperedToken, userDetails);

        assertFalse(valid);
    }

    @Test
    @DisplayName("Flujo token inválido: token de otro usuario no es válido para este usuario")
    void shouldReturnFalseForTokenOfDifferentUser() {
        UserDetails otherUser = User.builder()
                .username("otro@techcup.com")
                .password("password")
                .authorities(Collections.emptyList())
                .build();

        String tokenForOther = jwtService.generateToken(otherUser);

        boolean valid = jwtService.isTokenValid(tokenForOther, userDetails);

        assertFalse(valid);
    }

    // ─── Flujo sin token ───────────────────────────────────────────────────────

    @Test
    @DisplayName("Flujo sin token: string vacío lanza excepción o retorna false")
    void shouldReturnFalseForEmptyToken() {
        boolean valid = jwtService.isTokenValid("", userDetails);

        assertFalse(valid);
    }

    @Test
    @DisplayName("Flujo sin token: string basura lanza excepción o retorna false")
    void shouldReturnFalseForGarbageToken() {
        boolean valid = jwtService.isTokenValid("esto.no.es.un.jwt", userDetails);

        assertFalse(valid);
    }
}
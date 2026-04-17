package edu.dosw.TECHCUP.security.controller;

import edu.dosw.TECHCUP.security.controller.dto.LoginRequestDTO;
import edu.dosw.TECHCUP.security.controller.dto.LoginResponseDTO;
import edu.dosw.TECHCUP.security.controller.dto.RegisterRequestDTO;
import edu.dosw.TECHCUP.security.AuthService;
import edu.dosw.TECHCUP.security.JwtService;
import edu.dosw.TECHCUP.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final AuthService authService;
    private final UserRepository userRepository;

    @GetMapping("/check-availability")
    public ResponseEntity<Map<String, Boolean>> checkAvailability(
            @RequestParam String email,
            @RequestParam String documentId) {
        return ResponseEntity.ok(Map.of(
                "emailTaken",      userRepository.existsByEmail(email),
                "documentIdTaken", userRepository.existsByDocumentId(documentId)
        ));
    }

    @PostMapping("/register")
    public ResponseEntity<LoginResponseDTO> register(@RequestBody RegisterRequestDTO request) {
        log.info("POST /auth/register - Registro de nuevo usuario: {}", request.getEmail());

        authService.register(request);

        LoginResponseDTO response = authenticateAndGenerateToken(
                request.getEmail(), request.getPassword());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO request) {
        log.info("POST /auth/login - Intento de login para: {}", request.getEmail());

        LoginResponseDTO response = authenticateAndGenerateToken(
                request.getEmail(), request.getPassword());

        log.info("Login exitoso para: {}", request.getEmail());
        return ResponseEntity.ok(response);
    }

    private LoginResponseDTO authenticateAndGenerateToken(String email, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password));

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = jwtService.generateToken(userDetails);

        var user = userRepository.findByEmail(email).orElseThrow();
        return new LoginResponseDTO(
                token, "Bearer", email,
                user.getFirstName(), user.getLastName(),
                user.getUserType().name()
        );
    }
}
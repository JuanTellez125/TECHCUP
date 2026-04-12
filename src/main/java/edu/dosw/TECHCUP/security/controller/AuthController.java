package edu.dosw.TECHCUP.security.controller;

import edu.dosw.TECHCUP.security.controller.dto.LoginRequestDTO;
import edu.dosw.TECHCUP.security.controller.dto.LoginResponseDTO;
import edu.dosw.TECHCUP.security.controller.dto.RegisterRequestDTO;
import edu.dosw.TECHCUP.security.service.AuthService;
import edu.dosw.TECHCUP.security.service.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final AuthService authService;

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

        return new LoginResponseDTO(token, "Bearer", email);
    }
}
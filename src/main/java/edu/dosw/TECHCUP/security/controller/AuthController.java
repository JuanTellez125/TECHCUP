package edu.dosw.TECHCUP.security.controller;

import edu.dosw.TECHCUP.controller.dto.request.UserRequestDTO;
import edu.dosw.TECHCUP.core.model.enums.Role;
import edu.dosw.TECHCUP.controller.dto.response.UserResponseDTO;
import edu.dosw.TECHCUP.core.service.UserService;
import edu.dosw.TECHCUP.security.controller.dto.LoginRequestDTO;
import edu.dosw.TECHCUP.security.controller.dto.LoginResponseDTO;
import edu.dosw.TECHCUP.security.controller.dto.RegisterRequestDTO;
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
import org.springframework.beans.factory.annotation.Qualifier;

// Maneja el login y el registro y genera el JWT
@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    @Qualifier("playerService")
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<LoginResponseDTO> register(@RequestBody RegisterRequestDTO request) {
        log.info("POST /auth/register - Registro de nuevo usuario: {}", request.getEmail());

        //Construlle el DTO
        UserRequestDTO userRequest = UserRequestDTO.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(request.getPassword())
                .documentId(request.getDocumentId())
                // Deja PLAYER por defecto
                .userType(request.getUserType() != null ? request.getUserType() : Role.PLAYER)
                .build();

        UserResponseDTO createdUser = userService.createUser(userRequest);
        log.info("Usuario registrado con id: {}", createdUser.getId());

        // Genera el token JWT
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
        // autentica al usuario con las credenciales
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password));

        //ya se tiene el usuario autenticado
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = jwtService.generateToken(userDetails);

        return new LoginResponseDTO(token, "Bearer", email);
    }
}
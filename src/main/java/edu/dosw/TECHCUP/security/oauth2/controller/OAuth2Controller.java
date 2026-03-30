package edu.dosw.TECHCUP.security.oauth2.controller;

import edu.dosw.TECHCUP.core.model.enums.Role;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/oauth2")
public class OAuth2Controller {

    // Endpoint público — sin autenticación
    @GetMapping("/public")
    public ResponseEntity<?> publicEndpoint() {
        return ResponseEntity.ok(Map.of(
                "message", "Endpoint público TechCup"
        ));
    }

    // Endpoint protegido — requiere login con Google
    @GetMapping("/profile")
    public ResponseEntity<?> profile(@AuthenticationPrincipal OAuth2User user) {
        return ResponseEntity.ok(Map.of(
                "email",    user.getAttribute("email"),
                "name",     user.getAttribute("name"),
                "picture",  user.getAttribute("picture"),
                "role",     Role.PLAYER.name()
        ));
    }
}
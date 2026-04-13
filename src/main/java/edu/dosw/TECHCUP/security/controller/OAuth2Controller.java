package edu.dosw.TECHCUP.security.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/oauth2")
public class OAuth2Controller {

    @GetMapping("/google")
    public ResponseEntity<String> loginWithGoogle() {
        return ResponseEntity.ok("Redirigiendo a Google...");
    }
}
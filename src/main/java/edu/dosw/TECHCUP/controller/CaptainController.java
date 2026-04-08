package edu.dosw.TECHCUP.controller;

import edu.dosw.TECHCUP.controller.dto.request.UserRequestDTO;
import edu.dosw.TECHCUP.controller.dto.response.UserResponseDTO;
import edu.dosw.TECHCUP.core.service.impl.CaptainService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/captains")
@RequiredArgsConstructor
@Tag(name = "Captain", description = "Gestión de capitanes")
public class CaptainController {

    private final CaptainService captainService;

    @PostMapping
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @Operation(summary = "Crear capitán")
    public ResponseEntity<UserResponseDTO> createCaptain(@RequestBody UserRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(captainService.createUser(dto));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'CAPTAIN')")
    @Operation(summary = "Obtener capitán por ID")
    public ResponseEntity<UserResponseDTO> getCaptainById(@PathVariable Long id) {
        return ResponseEntity.ok(captainService.getCaptainById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @Operation(summary = "Actualizar capitán")
    public ResponseEntity<UserResponseDTO> updateCaptain(
            @PathVariable Long id,
            @RequestBody UserRequestDTO dto) {
        return ResponseEntity.ok(captainService.updateUser(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @Operation(summary = "Eliminar capitán")
    public ResponseEntity<Void> deleteCaptain(@PathVariable Long id) {
        captainService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}

package edu.dosw.TECHCUP.controller;

import edu.dosw.TECHCUP.controller.dto.request.SportProfileRequestDTO;
import edu.dosw.TECHCUP.controller.dto.request.UserRequestDTO;
import edu.dosw.TECHCUP.controller.dto.response.SportProfileResponseDTO;
import edu.dosw.TECHCUP.controller.dto.response.UserResponseDTO;
import edu.dosw.TECHCUP.core.service.impl.PlayerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/players")
@RequiredArgsConstructor
@Tag(name = "Player", description = "Gestión de jugadores")
public class PlayerController {

    private final PlayerService playerService;

    @PostMapping
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @Operation(summary = "Crear jugador")
    public ResponseEntity<UserResponseDTO> createPlayer(@RequestBody UserRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(playerService.createUser(dto));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'CAPTAIN', 'REFEREE')")
    @Operation(summary = "Obtener todos los jugadores")
    public ResponseEntity<List<UserResponseDTO>> getAllPlayers() {
        return ResponseEntity.ok(playerService.getAllPlayers());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'CAPTAIN', 'REFEREE', 'PLAYER')")
    @Operation(summary = "Obtener jugador por ID")
    public ResponseEntity<UserResponseDTO> getPlayerById(@PathVariable Long id) {
        return ResponseEntity.ok(playerService.getPlayerById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @Operation(summary = "Actualizar jugador")
    public ResponseEntity<UserResponseDTO> updatePlayer(
            @PathVariable Long id,
            @RequestBody UserRequestDTO dto) {
        return ResponseEntity.ok(playerService.updateUser(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @Operation(summary = "Eliminar jugador")
    public ResponseEntity<Void> deletePlayer(@PathVariable Long id) {
        playerService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{userId}/sport-profile")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'CAPTAIN', 'PLAYER')")
    @Operation(summary = "Crear o actualizar perfil deportivo")
    public ResponseEntity<SportProfileResponseDTO> saveSportProfile(
            @PathVariable Long userId,
            @RequestBody SportProfileRequestDTO dto) {
        return ResponseEntity.ok(playerService.saveSportProfile(userId, dto));
    }

    @GetMapping("/{userId}/sport-profile")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'CAPTAIN', 'REFEREE', 'PLAYER')")
    @Operation(summary = "Obtener perfil deportivo")
    public ResponseEntity<SportProfileResponseDTO> getSportProfile(@PathVariable Long userId) {
        return ResponseEntity.ok(playerService.getSportProfile(userId));
    }

    @PatchMapping("/{userId}/availability")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'PLAYER')")
    @Operation(summary = "Actualizar disponibilidad del jugador")
    public ResponseEntity<SportProfileResponseDTO> setAvailability(
            @PathVariable Long userId,
            @RequestParam boolean available) {
        return ResponseEntity.ok(playerService.setAvailability(userId, available));
    }
}

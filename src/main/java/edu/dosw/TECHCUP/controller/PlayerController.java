package edu.dosw.TECHCUP.controller;

import edu.dosw.TECHCUP.controller.dto.request.UserRequestDTO;
import edu.dosw.TECHCUP.controller.dto.response.UserResponseDTO;
import edu.dosw.TECHCUP.core.model.enums.PlayerAvailable;
import edu.dosw.TECHCUP.core.service.impl.PlayerService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class PlayerController {

    private final PlayerService playerService;

    @Operation(summary = "Register new player")
    @PostMapping("/players")
    public ResponseEntity<UserResponseDTO> createPlayer(@Valid @RequestBody UserRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(playerService.createUser(dto));
    }

    @Operation(summary = "Get player by ID")
    @GetMapping("/players/{id}")
    public ResponseEntity<UserResponseDTO> getPlayer(@PathVariable String id) {
        return ResponseEntity.ok(playerService.getPlayerById(id));
    }

    @Operation(summary = "List all players")
    @GetMapping("/players")
    public ResponseEntity<List<UserResponseDTO>> getAllPlayers() {
        return ResponseEntity.ok(playerService.getAllPlayers());
    }

    @Operation(summary = "Update player")
    @PutMapping("/players/{id}")
    public ResponseEntity<UserResponseDTO> updatePlayer(@PathVariable String id,
                                                        @Valid @RequestBody UserRequestDTO dto) {
        return ResponseEntity.ok(playerService.updateUser(id, dto));
    }

    @Operation(summary = "Delete player")
    @DeleteMapping("/players/{id}")
    public ResponseEntity<Void> deletePlayer(@PathVariable String id) {
        playerService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Update player availability")
    @PatchMapping("/players/{id}/availability")
    public ResponseEntity<UserResponseDTO> setAvailability(@PathVariable String id,
                                                           @RequestParam PlayerAvailable availability) {
        return ResponseEntity.ok(playerService.setAvailability(id, availability));
    }

}

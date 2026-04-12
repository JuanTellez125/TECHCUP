package edu.dosw.TECHCUP.controller;

import edu.dosw.TECHCUP.controller.dto.request.SportProfileRequestDTO;
import edu.dosw.TECHCUP.controller.dto.request.UserRequestDTO;
import edu.dosw.TECHCUP.controller.dto.response.SportProfileResponseDTO;
import edu.dosw.TECHCUP.controller.dto.response.UserResponseDTO;
import edu.dosw.TECHCUP.controller.mapper.UserMapper;
import edu.dosw.TECHCUP.core.model.User;
import edu.dosw.TECHCUP.core.service.impl.PlayerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/players")
@RequiredArgsConstructor
@Tag(name = "Player", description = "Player management")
public class PlayerController {

    private final PlayerService playerService;
    private final UserMapper userMapper;

    @PostMapping
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @Operation(summary = "Create player")
    public ResponseEntity<UserResponseDTO> createPlayer(@RequestBody UserRequestDTO dto) {
        User user = userMapper.toModel(dto);
        User created = playerService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(userMapper.toDto(created));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'CAPTAIN', 'REFEREE')")
    @Operation(summary = "Get all players")
    public ResponseEntity<List<UserResponseDTO>> getAllPlayers() {
        List<UserResponseDTO> players = playerService.getAllPlayers().stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(players);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'CAPTAIN', 'REFEREE', 'PLAYER')")
    @Operation(summary = "Get player by ID")
    public ResponseEntity<UserResponseDTO> getPlayerById(@PathVariable Long id) {
        return ResponseEntity.ok(userMapper.toDto(playerService.getPlayerById(id)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @Operation(summary = "Update player")
    public ResponseEntity<UserResponseDTO> updatePlayer(
            @PathVariable Long id,
            @RequestBody UserRequestDTO dto) {
        User user = userMapper.toModel(dto);
        User updated = playerService.updateUser(id, user);
        return ResponseEntity.ok(userMapper.toDto(updated));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @Operation(summary = "Delete player")
    public ResponseEntity<Void> deletePlayer(@PathVariable Long id) {
        playerService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{userId}/sport-profile")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'CAPTAIN', 'PLAYER')")
    @Operation(summary = "Create or update sports profile")
    public ResponseEntity<SportProfileResponseDTO> saveSportProfile(
            @PathVariable Long userId,
            @RequestBody SportProfileRequestDTO dto) {
        return ResponseEntity.ok(playerService.saveSportProfile(userId, dto));
    }

    @GetMapping("/{userId}/sport-profile")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'CAPTAIN', 'REFEREE', 'PLAYER')")
    @Operation(summary = "Get sports profile")
    public ResponseEntity<SportProfileResponseDTO> getSportProfile(@PathVariable Long userId) {
        return ResponseEntity.ok(playerService.getSportProfile(userId));
    }

    @PatchMapping("/{userId}/availability")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'PLAYER')")
    @Operation(summary = "Update player availability")
    public ResponseEntity<SportProfileResponseDTO> setAvailability(
            @PathVariable Long userId,
            @RequestParam boolean available) {
        return ResponseEntity.ok(playerService.setAvailability(userId, available));
    }
}

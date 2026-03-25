package edu.dosw.TECHCUP.controller;

import edu.dosw.TECHCUP.controller.dto.request.UserRequestDTO;
import edu.dosw.TECHCUP.controller.dto.response.UserResponseDTO;
import edu.dosw.TECHCUP.core.model.enums.PlayerAvailable;
import edu.dosw.TECHCUP.core.model.enums.Role;
import edu.dosw.TECHCUP.core.service.impl.AdministratorService;
import edu.dosw.TECHCUP.core.service.impl.CaptainService;
import edu.dosw.TECHCUP.core.service.impl.PlayerService;
import edu.dosw.TECHCUP.core.service.impl.RefereeService;
import edu.dosw.TECHCUP.core.service.impl.OrganizerService;
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
public class UserController {

    private final PlayerService playerService;
    private final CaptainService captainService;
    private final OrganizerService organizerService;
    private final RefereeService refereeService;
    private final AdministratorService administratorService;

    @Operation(summary = "Registrar nuevo jugador")
    @PostMapping("/players")
    public ResponseEntity<UserResponseDTO> createPlayer(@Valid @RequestBody UserRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(playerService.createUser(dto));
    }

    @Operation(summary = "Obtener jugador por ID")
    @GetMapping("/players/{id}")
    public ResponseEntity<UserResponseDTO> getPlayer(@PathVariable String id) {
        return ResponseEntity.ok(playerService.getPlayerById(id));
    }

    @Operation(summary = "Listar todos los jugadores")
    @GetMapping("/players")
    public ResponseEntity<List<UserResponseDTO>> getAllPlayers() {
        return ResponseEntity.ok(playerService.getAllPlayers());
    }

    @Operation(summary = "Actualizar jugador")
    @PutMapping("/players/{id}")
    public ResponseEntity<UserResponseDTO> updatePlayer(@PathVariable String id,
                                                        @Valid @RequestBody UserRequestDTO dto) {
        return ResponseEntity.ok(playerService.updateUser(id, dto));
    }

    @Operation(summary = "Eliminar jugador")
    @DeleteMapping("/players/{id}")
    public ResponseEntity<Void> deletePlayer(@PathVariable String id) {
        playerService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Actualizar disponibilidad del jugador (sección 6.2)")
    @PatchMapping("/players/{id}/availability")
    public ResponseEntity<UserResponseDTO> setAvailability(@PathVariable String id,
                                                           @RequestParam PlayerAvailable availability) {
        return ResponseEntity.ok(playerService.setAvailability(id, availability));
    }

    @Operation(summary = "Registrar nuevo capitán")
    @PostMapping("/captains")
    public ResponseEntity<UserResponseDTO> createCaptain(@Valid @RequestBody UserRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(captainService.createUser(dto));
    }

    @Operation(summary = "Obtener capitán por ID")
    @GetMapping("/captains/{id}")
    public ResponseEntity<UserResponseDTO> getCaptain(@PathVariable String id) {
        return ResponseEntity.ok(captainService.getCaptainById(id));
    }

    @Operation(summary = "Actualizar capitán")
    @PutMapping("/captains/{id}")
    public ResponseEntity<UserResponseDTO> updateCaptain(@PathVariable String id,
                                                         @Valid @RequestBody UserRequestDTO dto) {
        return ResponseEntity.ok(captainService.updateUser(id, dto));
    }

    @Operation(summary = "Registrar nuevo organizador")
    @PostMapping("/organizers")
    public ResponseEntity<UserResponseDTO> createOrganizer(@Valid @RequestBody UserRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(organizerService.createUser(dto));
    }

    @Operation(summary = "Listar organizadores")
    @GetMapping("/organizers")
    public ResponseEntity<List<UserResponseDTO>> getAllOrganizers() {
        return ResponseEntity.ok(organizerService.getAllOrganizers());
    }

    @Operation(summary = "Registrar nuevo árbitro")
    @PostMapping("/referees")
    public ResponseEntity<UserResponseDTO> createReferee(@Valid @RequestBody UserRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(refereeService.createUser(dto));
    }

    @Operation(summary = "Listar árbitros")
    @GetMapping("/referees")
    public ResponseEntity<List<UserResponseDTO>> getAllReferees() {
        return ResponseEntity.ok(refereeService.getAllReferees());
    }

    @Operation(summary = "Obtener árbitro por ID")
    @GetMapping("/referees/{id}")
    public ResponseEntity<UserResponseDTO> getReferee(@PathVariable String id) {
        return ResponseEntity.ok(refereeService.getRefereeById(id));
    }

    @Operation(summary = "Registrar nuevo administrador")
    @PostMapping("/admins")
    public ResponseEntity<UserResponseDTO> createAdmin(@Valid @RequestBody UserRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(administratorService.createUser(dto));
    }

    @Operation(summary = "Listar todos los usuarios (solo admin)")
    @GetMapping("/admins/all")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        return ResponseEntity.ok(administratorService.getAllUsers());
    }

    @Operation(summary = "Eliminar usuario (solo admin)")
    @DeleteMapping("/admins/{id}")
    public ResponseEntity<Void> deleteByAdmin(@PathVariable String id) {
        administratorService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Asignar rol a un usuario (RF21, solo admin)")
    @PatchMapping("/admins/{adminId}/assign-role")
    public ResponseEntity<UserResponseDTO> assignRole(@PathVariable String adminId, @RequestParam String targetUserId,
            @RequestParam Role newRole) {
        return ResponseEntity.ok(administratorService.assignRole(adminId, targetUserId, newRole));
    }

}

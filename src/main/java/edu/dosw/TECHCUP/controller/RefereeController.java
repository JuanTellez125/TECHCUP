package edu.dosw.TECHCUP.controller;

import edu.dosw.TECHCUP.controller.dto.request.UserRequestDTO;
import edu.dosw.TECHCUP.controller.dto.response.UserResponseDTO;
import edu.dosw.TECHCUP.core.service.impl.RefereeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/referees")
@RequiredArgsConstructor
@Tag(name = "Referee", description = "Gestión de árbitros")
public class RefereeController {

    private final RefereeService refereeService;

    @PostMapping
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @Operation(summary = "Crear árbitro")
    public ResponseEntity<UserResponseDTO> createReferee(@RequestBody UserRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(refereeService.createUser(dto));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'REFEREE')")
    @Operation(summary = "Obtener todos los árbitros")
    public ResponseEntity<List<UserResponseDTO>> getAllReferees() {
        return ResponseEntity.ok(refereeService.getAllReferees());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'REFEREE', 'CAPTAIN')")
    @Operation(summary = "Obtener árbitro por ID")
    public ResponseEntity<UserResponseDTO> getRefereeById(@PathVariable Long id) {
        return ResponseEntity.ok(refereeService.getRefereeById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @Operation(summary = "Actualizar árbitro")
    public ResponseEntity<UserResponseDTO> updateReferee(
            @PathVariable Long id,
            @RequestBody UserRequestDTO dto) {
        return ResponseEntity.ok(refereeService.updateUser(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @Operation(summary = "Eliminar árbitro")
    public ResponseEntity<Void> deleteReferee(@PathVariable Long id) {
        refereeService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}

package edu.dosw.TECHCUP.controller;

import edu.dosw.TECHCUP.controller.dto.request.UserRequestDTO;
import edu.dosw.TECHCUP.controller.dto.response.UserResponseDTO;
import edu.dosw.TECHCUP.core.service.impl.OrganizerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/organizers")
@RequiredArgsConstructor
@Tag(name = "Organizer", description = "Gestión de organizadores")
public class OrganizerController {

    private final OrganizerService organizerService;

    @PostMapping
    @Operation(summary = "Crear organizador")
    public ResponseEntity<UserResponseDTO> createOrganizer(@RequestBody UserRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(organizerService.createUser(dto));
    }

    @GetMapping
    @Operation(summary = "Obtener todos los organizadores")
    public ResponseEntity<List<UserResponseDTO>> getAllOrganizers() {
        return ResponseEntity.ok(organizerService.getAllOrganizers());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener organizador por ID")
    public ResponseEntity<UserResponseDTO> getOrganizerById(@PathVariable Long id) {
        return ResponseEntity.ok(organizerService.getOrganizerById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar organizador")
    public ResponseEntity<UserResponseDTO> updateOrganizer(
            @PathVariable Long id,
            @RequestBody UserRequestDTO dto) {
        return ResponseEntity.ok(organizerService.updateUser(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar organizador")
    public ResponseEntity<Void> deleteOrganizer(@PathVariable Long id) {
        organizerService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}

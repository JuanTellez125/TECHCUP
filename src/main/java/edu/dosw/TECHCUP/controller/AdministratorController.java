package edu.dosw.TECHCUP.controller;

import edu.dosw.TECHCUP.controller.dto.request.UserRequestDTO;
import edu.dosw.TECHCUP.controller.dto.response.UserResponseDTO;
import edu.dosw.TECHCUP.core.model.enums.Role;
import edu.dosw.TECHCUP.core.service.impl.AdministratorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/administrators")
@RequiredArgsConstructor
@Tag(name = "Administrator", description = "Gestión de administradores")
public class AdministratorController {

    private final AdministratorService administratorService;

    @PostMapping
    @Operation(summary = "Crear administrador")
    public ResponseEntity<UserResponseDTO> createAdministrator(@RequestBody UserRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(administratorService.createUser(dto));
    }

    @GetMapping
    @Operation(summary = "Obtener todos los usuarios")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        return ResponseEntity.ok(administratorService.getAllUsers());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener administrador por ID")
    public ResponseEntity<UserResponseDTO> getAdminById(@PathVariable Long id) {
        return ResponseEntity.ok(administratorService.getAdminById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar administrador")
    public ResponseEntity<UserResponseDTO> updateAdministrator(
            @PathVariable Long id,
            @RequestBody UserRequestDTO dto) {
        return ResponseEntity.ok(administratorService.updateUser(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar administrador")
    public ResponseEntity<Void> deleteAdministrator(@PathVariable Long id) {
        administratorService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{adminId}/assign-role")
    @Operation(summary = "Asignar rol a un usuario")
    public ResponseEntity<UserResponseDTO> assignRole(
            @PathVariable Long adminId,
            @RequestParam Long targetUserId,
            @RequestParam Role newRole) {
        return ResponseEntity.ok(administratorService.assignRole(adminId, targetUserId, newRole));
    }
}

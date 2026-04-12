package edu.dosw.TECHCUP.controller;

import edu.dosw.TECHCUP.controller.dto.request.UserRequestDTO;
import edu.dosw.TECHCUP.controller.dto.response.UserResponseDTO;
import edu.dosw.TECHCUP.controller.mapper.UserMapper;
import edu.dosw.TECHCUP.core.model.User;
import edu.dosw.TECHCUP.core.model.enums.Role;
import edu.dosw.TECHCUP.core.service.impl.AdministratorService;
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
@RequestMapping("/api/administrators")
@RequiredArgsConstructor
@Tag(name = "Administrator", description = "Administrator management")
public class AdministratorController {

    private final AdministratorService administratorService;
    private final UserMapper userMapper;

    @PostMapping
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @Operation(summary = "Create administrator")
    public ResponseEntity<UserResponseDTO> createAdministrator(@RequestBody UserRequestDTO dto) {
        User user = userMapper.toModel(dto);
        User created = administratorService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(userMapper.toDto(created));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @Operation(summary = "Get all users")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<UserResponseDTO> users = administratorService.getAllUsers().stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @Operation(summary = "Obtener administrador por ID")
    public ResponseEntity<UserResponseDTO> getAdminById(@PathVariable Long id) {
        return ResponseEntity.ok(userMapper.toDto(administratorService.getAdminById(id)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @Operation(summary = "Get administrator by ID")
    public ResponseEntity<UserResponseDTO> updateAdministrator(
            @PathVariable Long id,
            @RequestBody UserRequestDTO dto) {
        User user = userMapper.toModel(dto);
        User updated = administratorService.updateUser(id, user);
        return ResponseEntity.ok(userMapper.toDto(updated));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @Operation(summary = "Delete administrator")
    public ResponseEntity<Void> deleteAdministrator(@PathVariable Long id) {
        administratorService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{adminId}/assign-role")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @Operation(summary = "Assign a role to a user")
    public ResponseEntity<UserResponseDTO> assignRole(
            @PathVariable Long adminId,
            @RequestParam Long targetUserId,
            @RequestParam Role newRole) {
        return ResponseEntity.ok(userMapper.toDto(administratorService.assignRole(adminId, targetUserId, newRole)));
    }
}

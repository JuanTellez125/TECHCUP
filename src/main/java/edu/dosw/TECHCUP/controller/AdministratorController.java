package edu.dosw.TECHCUP.controller;

import edu.dosw.TECHCUP.controller.dto.request.UserRequestDTO;
import edu.dosw.TECHCUP.controller.dto.response.UserResponseDTO;
import edu.dosw.TECHCUP.core.model.enums.Role;
import edu.dosw.TECHCUP.core.service.impl.AdministratorService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/administrators")
@RequiredArgsConstructor
public class AdministratorController {

    private final AdministratorService administratorService;

    @Operation(summary = "Register new administrator")
    @PostMapping("/admins")
    public ResponseEntity<UserResponseDTO> createAdmin(@Valid @RequestBody UserRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(administratorService.createUser(dto));
    }

    @Operation(summary = "List all users")
    @GetMapping("/admins/all")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        return ResponseEntity.ok(administratorService.getAllUsers());
    }

    @Operation(summary = "Delete User")
    @DeleteMapping("/admins/{id}")
    public ResponseEntity<Void> deleteByAdmin(@PathVariable String id) {
        administratorService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Assign a role to a user")
    @PatchMapping("/admins/{adminId}/assign-role")
    public ResponseEntity<UserResponseDTO> assignRole(@PathVariable String adminId, @RequestParam String targetUserId,
                                                      @RequestParam Role newRole) {
        return ResponseEntity.ok(administratorService.assignRole(adminId, targetUserId, newRole));
    }



}

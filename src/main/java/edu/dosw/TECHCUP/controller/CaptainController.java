package edu.dosw.TECHCUP.controller;

import edu.dosw.TECHCUP.controller.dto.request.UserRequestDTO;
import edu.dosw.TECHCUP.controller.dto.response.UserResponseDTO;
import edu.dosw.TECHCUP.controller.mapper.UserMapper;
import edu.dosw.TECHCUP.core.model.User;
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
@Tag(name = "Captain", description = "Captains' Management")
public class CaptainController {

    private final CaptainService captainService;
    private final UserMapper userMapper;

    @PostMapping
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @Operation(summary = "Create captain")
    public ResponseEntity<UserResponseDTO> createCaptain(@RequestBody UserRequestDTO dto) {
        User user = userMapper.toModel(dto);
        User created = captainService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(userMapper.toDto(created));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'CAPTAIN')")
    @Operation(summary = "Get captain by ID")
    public ResponseEntity<UserResponseDTO> getCaptainById(@PathVariable Long id) {
        return ResponseEntity.ok(userMapper.toDto(captainService.getCaptainById(id)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @Operation(summary = "Update captain")
    public ResponseEntity<UserResponseDTO> updateCaptain(
            @PathVariable Long id,
            @RequestBody UserRequestDTO dto) {
        User user = userMapper.toModel(dto);
        User updated = captainService.updateUser(id, user);
        return ResponseEntity.ok(userMapper.toDto(updated));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @Operation(summary = "Delete captain")
    public ResponseEntity<Void> deleteCaptain(@PathVariable Long id) {
        captainService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}

package edu.dosw.TECHCUP.controller;

import edu.dosw.TECHCUP.controller.dto.request.UserRequestDTO;
import edu.dosw.TECHCUP.controller.dto.response.UserResponseDTO;
import edu.dosw.TECHCUP.controller.mapper.UserMapper;
import edu.dosw.TECHCUP.core.model.User;
import edu.dosw.TECHCUP.core.service.impl.OrganizerService;
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
@RequestMapping("/api/organizers")
@RequiredArgsConstructor
@Tag(name = "Organizer", description = "Organizer management")
public class OrganizerController {

    private final OrganizerService organizerService;
    private final UserMapper userMapper;

    @PostMapping
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @Operation(summary = "Create organizer")
    public ResponseEntity<UserResponseDTO> createOrganizer(@RequestBody UserRequestDTO dto) {
        User user = userMapper.toModel(dto);
        User created = organizerService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(userMapper.toDto(created));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @Operation(summary = "Get all organizers")
    public ResponseEntity<List<UserResponseDTO>> getAllOrganizers() {
        List<UserResponseDTO> organizers = organizerService.getAllOrganizers().stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(organizers);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'ORGANIZER')")
    @Operation(summary = "Get organizer by ID")
    public ResponseEntity<UserResponseDTO> getOrganizerById(@PathVariable Long id) {
        return ResponseEntity.ok(userMapper.toDto(organizerService.getOrganizerById(id)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @Operation(summary = "Update organizer")
    public ResponseEntity<UserResponseDTO> updateOrganizer(
            @PathVariable Long id,
            @RequestBody UserRequestDTO dto) {
        User user = userMapper.toModel(dto);
        User updated = organizerService.updateUser(id, user);
        return ResponseEntity.ok(userMapper.toDto(updated));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @Operation(summary = "Delete organizer")
    public ResponseEntity<Void> deleteOrganizer(@PathVariable Long id) {
        organizerService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}

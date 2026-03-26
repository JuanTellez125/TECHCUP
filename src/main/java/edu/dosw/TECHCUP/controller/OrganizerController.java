package edu.dosw.TECHCUP.controller;

import edu.dosw.TECHCUP.controller.dto.request.UserRequestDTO;
import edu.dosw.TECHCUP.controller.dto.response.UserResponseDTO;
import edu.dosw.TECHCUP.core.service.impl.OrganizerService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/organizers")
@RequiredArgsConstructor
public class OrganizerController {

    private final OrganizerService organizerService;

    @Operation(summary = "Register a new organizer")
    @PostMapping("/organizers")
    public ResponseEntity<UserResponseDTO> createOrganizer(@Valid @RequestBody UserRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(organizerService.createUser(dto));
    }

    @Operation(summary = "List organizers")
    @GetMapping("/organizers")
    public ResponseEntity<List<UserResponseDTO>> getAllOrganizers() {
        return ResponseEntity.ok(organizerService.getAllOrganizers());
    }

    @Operation(summary = "Update organizer")
    @PutMapping("/organizers/{id}")
    public ResponseEntity<UserResponseDTO> updateOrganizer(@PathVariable String id,
                                                        @Valid @RequestBody UserRequestDTO dto) {
        return ResponseEntity.ok(organizerService.updateUser(id, dto));
    }

    @Operation(summary = "Delete organizer")
    @DeleteMapping("/organizers/{id}")
    public ResponseEntity<Void> deleteOrganizer(@PathVariable String id) {
        organizerService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}

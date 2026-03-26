package edu.dosw.TECHCUP.controller;

import edu.dosw.TECHCUP.controller.dto.request.UserRequestDTO;
import edu.dosw.TECHCUP.controller.dto.response.UserResponseDTO;
import edu.dosw.TECHCUP.core.service.impl.RefereeService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/referees")
@RequiredArgsConstructor
public class RefereeController {

    private final RefereeService refereeService;

    @Operation(summary = "Register a new referee")
    @PostMapping("/referees")
    public ResponseEntity<UserResponseDTO> createReferee(@Valid @RequestBody UserRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(refereeService.createUser(dto));
    }

    @Operation(summary = "List referees")
    @GetMapping("/referees")
    public ResponseEntity<List<UserResponseDTO>> getAllReferee() {
        return ResponseEntity.ok(refereeService.getAllReferees());
    }

    @Operation(summary = "Get referee by ID")
    @GetMapping("/referees/{id}")
    public ResponseEntity<UserResponseDTO> getReferee(@PathVariable String id) {
        return ResponseEntity.ok(refereeService.getRefereeById(id));
    }

    @Operation(summary = "Update referee")
    @PutMapping("/referees/{id}")
    public ResponseEntity<UserResponseDTO> updateReferee(@PathVariable String id,
                                                           @Valid @RequestBody UserRequestDTO dto) {
        return ResponseEntity.ok(refereeService.updateUser(id, dto));
    }

    @Operation(summary = "Delete referee")
    @DeleteMapping("/referees/{id}")
    public ResponseEntity<Void> deleteReferee(@PathVariable String id) {
        refereeService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}

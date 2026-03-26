package edu.dosw.TECHCUP.controller;

import edu.dosw.TECHCUP.controller.dto.request.UserRequestDTO;
import edu.dosw.TECHCUP.controller.dto.response.UserResponseDTO;
import edu.dosw.TECHCUP.core.service.impl.CaptainService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/captains")
@RequiredArgsConstructor
public class CaptainController {

    private final CaptainService captainService;

    @Operation(summary = "Register new captain")
    @PostMapping("/captains")
    public ResponseEntity<UserResponseDTO> createCaptain(@Valid @RequestBody UserRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(captainService.createUser(dto));
    }

    @Operation(summary = "Consult captain information")
    @GetMapping("/captains/{id}")
    public ResponseEntity<UserResponseDTO> getCaptain(@PathVariable String id) {
        return ResponseEntity.ok(captainService.getCaptainById(id));
    }

    @Operation(summary = "Update captain")
    @PutMapping("/captains/{id}")
    public ResponseEntity<UserResponseDTO> updateCaptain(@PathVariable String id,
                                                         @Valid @RequestBody UserRequestDTO dto) {
        return ResponseEntity.ok(captainService.updateUser(id, dto));
    }

    @Operation(summary = "Delete captain")
    @DeleteMapping("/captains/{id}")
    public ResponseEntity<Void> deletePlayer(@PathVariable String id) {
        captainService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }


}

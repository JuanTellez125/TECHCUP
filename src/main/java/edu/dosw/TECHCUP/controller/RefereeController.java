package edu.dosw.TECHCUP.controller;

import edu.dosw.TECHCUP.controller.dto.request.UserRequestDTO;
import edu.dosw.TECHCUP.controller.dto.response.UserResponseDTO;
import edu.dosw.TECHCUP.controller.mapper.UserMapper;
import edu.dosw.TECHCUP.core.model.User;
import edu.dosw.TECHCUP.core.service.impl.RefereeService;
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
@RequestMapping("/api/referees")
@RequiredArgsConstructor
@Tag(name = "Referee", description = "Referees management")
public class RefereeController {

    private final RefereeService refereeService;
    private final UserMapper userMapper;

    @PostMapping
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @Operation(summary = "Create referee")
    public ResponseEntity<UserResponseDTO> createReferee(@RequestBody UserRequestDTO dto) {
        User user = userMapper.toModel(dto);
        User created = refereeService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(userMapper.toDto(created));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'REFEREE')")
    @Operation(summary = "Get all the referees")
    public ResponseEntity<List<UserResponseDTO>> getAllReferees() {
        List<UserResponseDTO> referees = refereeService.getAllReferees().stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(referees);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'REFEREE', 'CAPTAIN')")
    @Operation(summary = "Get referee by ID")
    public ResponseEntity<UserResponseDTO> getRefereeById(@PathVariable Long id) {
        return ResponseEntity.ok(userMapper.toDto(refereeService.getRefereeById(id)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @Operation(summary = "Update referee")
    public ResponseEntity<UserResponseDTO> updateReferee(
            @PathVariable Long id,
            @RequestBody UserRequestDTO dto) {
        User user = userMapper.toModel(dto);
        User updated = refereeService.updateUser(id, user);
        return ResponseEntity.ok(userMapper.toDto(updated));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @Operation(summary = "Delete referee")
    public ResponseEntity<Void> deleteReferee(@PathVariable Long id) {
        refereeService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}

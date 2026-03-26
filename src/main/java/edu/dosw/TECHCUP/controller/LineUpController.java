package edu.dosw.TECHCUP.controller;

import edu.dosw.TECHCUP.controller.dto.request.LineUpRequestDTO;
import edu.dosw.TECHCUP.controller.dto.response.LineUpResponseDTO;
import edu.dosw.TECHCUP.core.service.LineUpService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/lineups")
@RequiredArgsConstructor
public class LineUpController {

    private final LineUpService lineUpService;

    @Operation(summary = "Register or update lineup")
    @PostMapping
    public ResponseEntity<LineUpResponseDTO> saveLineUp(
            @RequestParam String captainId,
            @Valid @RequestBody LineUpRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(lineUpService.saveLineUp(captainId, dto));
    }

    @Operation(summary = "Check a team's lineup for a match")
    @GetMapping
    public ResponseEntity<LineUpResponseDTO> getLineUp(
            @RequestParam String teamId,
            @RequestParam String matchId) {
        return ResponseEntity.ok(lineUpService.getLineUp(teamId, matchId));
    }
}

package edu.dosw.TECHCUP.controller;

import edu.dosw.TECHCUP.controller.dto.request.LineUpRequestDTO;
import edu.dosw.TECHCUP.controller.dto.response.LineUpResponseDTO;
import edu.dosw.TECHCUP.core.service.LineUpService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lineups")
@RequiredArgsConstructor
@Tag(name = "LineUp", description = "Manage team lineups for matches")
public class LineUpController {

    private final LineUpService lineUpService;

    @PostMapping
    @PreAuthorize("hasAnyRole('CAPTAIN', 'ADMINISTRATOR')")
    @Operation(summary = "Save full team lineup for a match (captain only)")
    public ResponseEntity<List<LineUpResponseDTO>> saveLineUp(
            @RequestParam Long captainId,
            @RequestParam Long matchId,
            @RequestBody LineUpRequestDTO dto) {
        return ResponseEntity.ok(lineUpService.saveLineUp(captainId, matchId, dto));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'ORGANIZER', 'REFEREE', 'CAPTAIN', 'PLAYER')")
    @Operation(summary = "Get lineup by match and team")
    public ResponseEntity<List<LineUpResponseDTO>> getLineUp(
            @RequestParam Long matchId,
            @RequestParam Long teamId) {
        return ResponseEntity.ok(lineUpService.getLineUp(matchId, teamId));
    }
}

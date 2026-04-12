package edu.dosw.TECHCUP.controller;

import edu.dosw.TECHCUP.controller.dto.request.LineUpRequestDTO;
import edu.dosw.TECHCUP.controller.dto.response.LineUpResponseDTO;
import edu.dosw.TECHCUP.core.service.LineUpService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/lineups")
@RequiredArgsConstructor
@Tag(name = "LineUp", description = "Lineup management")
public class LineUpController {

    private final LineUpService lineUpService;

    @PostMapping
    @Operation(summary = "Save lineup (team captain only)")
    public ResponseEntity<LineUpResponseDTO> saveLineUp(
            @RequestParam Long captainId,
            @RequestBody LineUpRequestDTO dto) {
        return ResponseEntity.ok(lineUpService.saveLineUp(captainId, dto));
    }

    @GetMapping
    @Operation(summary = "Get team line-up by team and match")
    public ResponseEntity<LineUpResponseDTO> getLineUp(
            @RequestParam Long teamId,
            @RequestParam Long matchId) {
        return ResponseEntity.ok(lineUpService.getLineUp(teamId, matchId));
    }
}

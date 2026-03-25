package edu.dosw.TECHCUP.controller;

import edu.dosw.TECHCUP.controller.dto.request.MatchRequestDTO;
import edu.dosw.TECHCUP.controller.dto.request.MatchResultRequestDTO;
import edu.dosw.TECHCUP.controller.dto.response.MatchResponseDTO;
import edu.dosw.TECHCUP.core.service.MatchService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/matches")
@RequiredArgsConstructor
public class MatchController {

    private final MatchService matchService;

    @Operation(summary = "Programar un partido")
    @PostMapping
    public ResponseEntity<MatchResponseDTO> scheduleMatch(@Valid @RequestBody MatchRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(matchService.scheduleMatch(dto));
    }

    @Operation(summary = "Registrar resultado de un partido (RF15)")
    @PatchMapping("/{matchId}/result")
    public ResponseEntity<MatchResponseDTO> registerResult(@PathVariable String matchId,
            @Valid @RequestBody MatchResultRequestDTO dto) {
        return ResponseEntity.ok(matchService.registerResult(
                matchId,
                dto.getLocalScore(),
                dto.getAwayScore(),
                dto.getScorerIds(),
                dto.getYellowCardPlayerIds(),
                dto.getRedCardPlayerIds()
        ));
    }




}
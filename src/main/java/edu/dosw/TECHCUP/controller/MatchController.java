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

    @Operation(summary = "Schedule a match")
    @PostMapping
    public ResponseEntity<MatchResponseDTO> scheduleMatch(@Valid @RequestBody MatchRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(matchService.scheduleMatch(dto));
    }

    @Operation(summary = "Record the result of a match")
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

    @Operation(summary = "Check matches assigned to a referee")
    @GetMapping("/referee/{refereeId}")
    public ResponseEntity<List<MatchResponseDTO>> getMatchesByReferee(@PathVariable String refereeId) {
        return ResponseEntity.ok(matchService.getMatchesByReferee(refereeId));
    }

    @Operation(summary = "Check matches in a tournament")
    @GetMapping("/tournament/{tournamentId}")
    public ResponseEntity<List<MatchResponseDTO>> getMatchesByTournament(@PathVariable String tournamentId) {
        return ResponseEntity.ok(matchService.getMatchesByTournament(tournamentId));
    }

    @Operation(summary = "History of matches played")
    @GetMapping("/tournament/{tournamentId}/history")
    public ResponseEntity<List<MatchResponseDTO>> getMatchHistory(@PathVariable String tournamentId) {
        return ResponseEntity.ok(matchService.getMatchHistory(tournamentId));
    }

    @Operation(summary = "Generate knockout brackets")
    @PostMapping("/tournament/{tournamentId}/bracket")
    public ResponseEntity<List<MatchResponseDTO>> generateBracket(@PathVariable String tournamentId,
            @RequestParam String organizerId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(matchService.generateBracket(organizerId, tournamentId));
    }




}
package edu.dosw.TECHCUP.controller;

import edu.dosw.TECHCUP.controller.dto.request.TournamentConfigRequestDTO;
import edu.dosw.TECHCUP.controller.dto.request.TournamentRequestDTO;
import edu.dosw.TECHCUP.controller.dto.response.TournamentResponseDTO;
import edu.dosw.TECHCUP.core.service.TournamentService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tournaments")
@RequiredArgsConstructor
public class TournamentController {

    private final TournamentService tournamentService;

    @Operation(summary = "Create a tournament")
    @PostMapping
    public ResponseEntity<TournamentResponseDTO> createTournament(
            @RequestParam String organizerId,
            @Valid @RequestBody TournamentRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(tournamentService.createTournament(organizerId, dto));
    }

    @Operation(summary = "Check tournament by ID")
    @GetMapping("/{id}")
    public ResponseEntity<TournamentResponseDTO> getTournament(@PathVariable String id) {
        return ResponseEntity.ok(tournamentService.getTournamentById(id));
    }

    @Operation(summary = "List all tournaments")
    @GetMapping
    public ResponseEntity<List<TournamentResponseDTO>> getAllTournaments() {
        return ResponseEntity.ok(tournamentService.getAllTournaments());
    }

    @Operation(summary = "History of completed tournaments")
    @GetMapping("/history")
    public ResponseEntity<List<TournamentResponseDTO>> getFinalizedTournaments() {
        return ResponseEntity.ok(tournamentService.getFinalizedTournaments());
    }

    @Operation(summary = "Start tournament: SKETCH → ACTIVE")
    @PatchMapping("/{id}/start")
    public ResponseEntity<TournamentResponseDTO> startTournament(
            @PathVariable String id,
            @RequestParam String organizerId) {
        return ResponseEntity.ok(tournamentService.startTournament(organizerId, id));
    }

    @Operation(summary = "Start progress: ACTIVE → INPROGRESS")
    @PatchMapping("/{id}/progress")
    public ResponseEntity<TournamentResponseDTO> setInProgress(
            @PathVariable String id,
            @RequestParam String organizerId) {
        return ResponseEntity.ok(tournamentService.setInProgress(organizerId, id));
    }

    @Operation(summary = "Tournament ends: INPROGRESS → FINALIZED")
    @PatchMapping("/{id}/finish")
    public ResponseEntity<TournamentResponseDTO> finishTournament(
            @PathVariable String id,
            @RequestParam String organizerId) {
        return ResponseEntity.ok(tournamentService.finishTournament(organizerId, id));
    }

    @Operation(summary = "Configure tournament rules and dates")
    @PatchMapping("/{id}/config")
    public ResponseEntity<TournamentResponseDTO> configTournament(
            @PathVariable String id,
            @RequestParam String organizerId,
            @Valid @RequestBody TournamentConfigRequestDTO dto) {
        return ResponseEntity.ok(tournamentService.configTournament(organizerId, id, dto));
    }

}

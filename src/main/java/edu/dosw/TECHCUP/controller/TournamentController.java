package edu.dosw.TECHCUP.controller;


import edu.dosw.TECHCUP.controller.dto.response.TournamentHistoryResponseDTO;
import edu.dosw.TECHCUP.core.service.TournamentHistoryService;
import edu.dosw.TECHCUP.controller.dto.request.TournamentConfigRequestDTO;
import edu.dosw.TECHCUP.controller.dto.request.TournamentRequestDTO;
import edu.dosw.TECHCUP.controller.dto.request.VenueRequestDTO;
import edu.dosw.TECHCUP.controller.dto.response.TournamentConfigResponseDTO;
import edu.dosw.TECHCUP.controller.dto.response.TournamentResponseDTO;
import edu.dosw.TECHCUP.controller.dto.response.TournamentStatisticsResponseDTO;
import edu.dosw.TECHCUP.controller.dto.response.VenueResponseDTO;
import edu.dosw.TECHCUP.core.service.StatisticsService;
import edu.dosw.TECHCUP.core.service.TournamentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@RestController
@RequestMapping("/api/tournaments")
@RequiredArgsConstructor
@Tag(name = "Tournament", description = "Tournament management")
public class TournamentController {

    private final TournamentService tournamentService;
    private final StatisticsService statisticsService;
    private final TournamentHistoryService tournamentHistoryService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ORGANIZER', 'ADMINISTRATOR')")
    @Operation(summary = "Create tournament (organizer only)")
    public ResponseEntity<TournamentResponseDTO> createTournament(
            @RequestParam Long organizerId,
            @RequestBody TournamentRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(tournamentService.createTournament(organizerId, dto));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'ORGANIZER', 'REFEREE', 'CAPTAIN', 'PLAYER')")
    @Operation(summary = "Get all tournaments")
    public ResponseEntity<List<TournamentResponseDTO>> getAllTournaments() {
        return ResponseEntity.ok(tournamentService.getAllTournaments());
    }

    @GetMapping("/finalized")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'ORGANIZER', 'REFEREE', 'CAPTAIN', 'PLAYER')")
    @Operation(summary = "Obtain completed tournaments")
    public ResponseEntity<List<TournamentResponseDTO>> getFinalizedTournaments() {
        return ResponseEntity.ok(tournamentService.getFinalizedTournaments());
    }

    @GetMapping("/{tournamentId}")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'ORGANIZER', 'REFEREE', 'CAPTAIN', 'PLAYER')")
    @Operation(summary = "Get tournament by ID")
    public ResponseEntity<TournamentResponseDTO> getTournamentById(@PathVariable String tournamentId) {
        return ResponseEntity.ok(tournamentService.getTournamentById(tournamentId));
    }

    @PatchMapping("/{tournamentId}/start")
    @PreAuthorize("hasAnyRole('ORGANIZER', 'ADMINISTRATOR')")
    @Operation(summary = "Start tournament")
    public ResponseEntity<TournamentResponseDTO> startTournament(
            @RequestParam Long organizerId,
            @PathVariable Long tournamentId) {
        return ResponseEntity.ok(tournamentService.startTournament(organizerId, tournamentId));
    }

    @PatchMapping("/{tournamentId}/finish")
    @PreAuthorize("hasAnyRole('ORGANIZER', 'ADMINISTRATOR')")
    @Operation(summary = "Finish tournament")
    public ResponseEntity<TournamentResponseDTO> finishTournament(
            @RequestParam Long organizerId,
            @PathVariable Long tournamentId) {
        return ResponseEntity.ok(tournamentService.finishTournament(organizerId, tournamentId));
    }

    @PostMapping("/{tournamentId}/config")
    @PreAuthorize("hasAnyRole('ORGANIZER', 'ADMINISTRATOR')")
    @Operation(summary = "Set up tournament")
    public ResponseEntity<TournamentConfigResponseDTO> configTournament(
            @RequestParam Long organizerId,
            @PathVariable Long tournamentId,
            @RequestBody TournamentConfigRequestDTO dto) {
        return ResponseEntity.ok(tournamentService.configTournament(organizerId, tournamentId, dto));
    }

    @GetMapping("/{tournamentId}/config")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'ORGANIZER', 'REFEREE', 'CAPTAIN', 'PLAYER')")
    @Operation(summary = "Get tournament configuration")
    public ResponseEntity<TournamentConfigResponseDTO> getConfig(@PathVariable Long tournamentId) {
        return ResponseEntity.ok(tournamentService.getConfig(tournamentId));
    }

    @PostMapping("/{tournamentId}/venues")
    @PreAuthorize("hasAnyRole('ORGANIZER', 'ADMINISTRATOR')")
    @Operation(summary = "Add venue to tournament")
    public ResponseEntity<VenueResponseDTO> addVenue(
            @RequestParam Long organizerId,
            @PathVariable Long tournamentId,
            @RequestBody VenueRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(tournamentService.addVenue(organizerId, tournamentId, dto));
    }

    @GetMapping("/{tournamentId}/venues")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'ORGANIZER', 'REFEREE', 'CAPTAIN', 'PLAYER')")
    @Operation(summary = "Secure tournament venues")
    public ResponseEntity<List<VenueResponseDTO>> getVenuesByTournament(@PathVariable Long tournamentId) {
        return ResponseEntity.ok(tournamentService.getVenuesByTournament(tournamentId));
    }

    @GetMapping("/{tournamentId}/statistics")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'ORGANIZER', 'REFEREE', 'CAPTAIN', 'PLAYER')")
    @Operation(summary = "Get consolidated tournament statistics")
    public ResponseEntity<TournamentStatisticsResponseDTO> getTournamentStatistics(
            @PathVariable Long tournamentId) {
        return ResponseEntity.ok(statisticsService.getTournamentStatistics(tournamentId));
    }

    @GetMapping("/historial")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'ORGANIZER', 'REFEREE', 'CAPTAIN', 'PLAYER')")
    @Operation(summary = "List all finalized tournaments (RF20)")
    public ResponseEntity<List<TournamentResponseDTO>> getFinishedTournaments() {
        return ResponseEntity.ok(tournamentHistoryService.getFinishedTournaments());
    }


    @GetMapping("/{tournamentId}/historial")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'ORGANIZER', 'REFEREE', 'CAPTAIN', 'PLAYER')")
    @Operation(summary = "Get full history of a finalized tournament (RF20)")
    public ResponseEntity<TournamentHistoryResponseDTO> getTournamentHistory(
            @PathVariable Long tournamentId) {
        return ResponseEntity.ok(tournamentHistoryService.getTournamentHistory(tournamentId));
    }
}
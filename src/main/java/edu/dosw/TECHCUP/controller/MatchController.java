package edu.dosw.TECHCUP.controller;

//import edu.dosw.TECHCUP.controller.dto.request.LineUpRequestDTO;
import edu.dosw.TECHCUP.controller.dto.request.LineUpRequestDTO;
import edu.dosw.TECHCUP.controller.dto.request.MatchEventRequestDTO;
import edu.dosw.TECHCUP.controller.dto.request.MatchRequestDTO;
import edu.dosw.TECHCUP.controller.dto.request.MatchResultRequestDTO;
import edu.dosw.TECHCUP.controller.dto.response.*;
import edu.dosw.TECHCUP.core.service.MatchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@RestController
@RequestMapping("/api/matches")
@RequiredArgsConstructor
@Tag(name = "Match", description = "Gestión de partidos")
public class MatchController {

    private final MatchService matchService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ORGANIZER', 'ADMINISTRATOR')")
    @Operation(summary = "Programar partido")
    public ResponseEntity<MatchResponseDTO> scheduleMatch(@RequestBody MatchRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(matchService.scheduleMatch(dto));
    }

    @PostMapping("/result")
    @PreAuthorize("hasAnyRole('ORGANIZER', 'ADMINISTRATOR')")
    @Operation(summary = "Registrar resultado de partido (solo organizador)")
    public ResponseEntity<MatchResultResponseDTO> registerResult(
            @RequestParam Long organizerId,
            @RequestBody MatchResultRequestDTO dto) {
        return ResponseEntity.ok(matchService.registerResult(organizerId, dto));
    }

    @PostMapping("/event")
    @PreAuthorize("hasAnyRole('REFEREE', 'ORGANIZER', 'ADMINISTRATOR')")
    @Operation(summary = "Registrar evento de partido (gol, tarjeta, etc.)")
    public ResponseEntity<MatchEventResponseDTO> registerEvent(@RequestBody MatchEventRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(matchService.registerEvent(dto));
    }

    @PostMapping("/lineup")
    @PreAuthorize("hasAnyRole('CAPTAIN', 'ADMINISTRATOR')")
    @Operation(summary = "Guardar alineación (solo capitán)")
    public ResponseEntity<LineUpResponseDTO> saveLineUp(
            @RequestParam Long captainId,
            @RequestBody LineUpRequestDTO dto) {
        return ResponseEntity.ok(matchService.saveLineUp(captainId, dto));
    }

    @GetMapping("/lineup")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'ORGANIZER', 'REFEREE', 'CAPTAIN', 'PLAYER')")
    @Operation(summary = "Obtener alineación por partido y equipo")
    public ResponseEntity<List<LineUpResponseDTO>> getLineUpByMatchAndTeam(
            @RequestParam Long matchId,
            @RequestParam Long teamId) {
        return ResponseEntity.ok(matchService.getLineUpByMatchAndTeam(matchId, teamId));
    }

    @GetMapping("/referee/{refereeId}")
    @PreAuthorize("hasAnyRole('REFEREE', 'ORGANIZER', 'ADMINISTRATOR')")
    @Operation(summary = "Obtener partidos por árbitro")
    public ResponseEntity<List<MatchResponseDTO>> getMatchesByReferee(@PathVariable Long refereeId) {
        return ResponseEntity.ok(matchService.getMatchesByReferee(refereeId));
    }

    @PostMapping("/bracket/{tournamentId}")
    @PreAuthorize("hasAnyRole('ORGANIZER', 'ADMINISTRATOR')")
    @Operation(summary = "Generar llaves del torneo")
    public ResponseEntity<List<MatchResponseDTO>> generateBracket(
            @RequestParam Long organizerId,
            @PathVariable Long tournamentId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(matchService.generateBracket(organizerId, tournamentId));
    }

    @GetMapping("/tournament/{tournamentId}")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'ORGANIZER', 'REFEREE', 'CAPTAIN', 'PLAYER')")
    @Operation(summary = "Obtener todos los partidos del torneo")
    public ResponseEntity<List<MatchResponseDTO>> getMatchesByTournament(@PathVariable Long tournamentId) {
        return ResponseEntity.ok(matchService.getMatchesByTournament(tournamentId));
    }

    @GetMapping("/tournament/{tournamentId}/history")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'ORGANIZER', 'REFEREE', 'CAPTAIN', 'PLAYER')")
    @Operation(summary = "Obtener historial de partidos jugados")
    public ResponseEntity<List<MatchResponseDTO>> getMatchHistory(@PathVariable Long tournamentId) {
        return ResponseEntity.ok(matchService.getMatchHistory(tournamentId));
    }

    @GetMapping("/tournament/{tournamentId}/standings")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'ORGANIZER', 'REFEREE', 'CAPTAIN', 'PLAYER')")
    @Operation(summary = "Obtener tabla de posiciones")
    public ResponseEntity<List<StandingResponseDTO>> getStandings(@PathVariable Long tournamentId) {
        return ResponseEntity.ok(matchService.getStandings(tournamentId));
    }

    @GetMapping("/tournament/{tournamentId}/top-scorers")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'ORGANIZER', 'REFEREE', 'CAPTAIN', 'PLAYER')")
    @Operation(summary = "Obtener goleadores del torneo")
    public ResponseEntity<List<MatchEventResponseDTO>> getTopScorers(@PathVariable Long tournamentId) {
        return ResponseEntity.ok(matchService.getTopScorers(tournamentId));
    }
}

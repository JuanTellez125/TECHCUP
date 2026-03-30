package edu.dosw.TECHCUP.controller;

import edu.dosw.TECHCUP.controller.dto.request.TournamentConfigRequestDTO;
import edu.dosw.TECHCUP.controller.dto.request.TournamentRequestDTO;
import edu.dosw.TECHCUP.controller.dto.request.VenueRequestDTO;
import edu.dosw.TECHCUP.controller.dto.response.TournamentConfigResponseDTO;
import edu.dosw.TECHCUP.controller.dto.response.TournamentResponseDTO;
import edu.dosw.TECHCUP.controller.dto.response.VenueResponseDTO;
import edu.dosw.TECHCUP.core.service.TournamentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tournaments")
@RequiredArgsConstructor
@Tag(name = "Tournament", description = "Gestión de torneos")
public class TournamentController {

    private final TournamentService tournamentService;

    @PostMapping
    @Operation(summary = "Crear torneo (solo organizador)")
    public ResponseEntity<TournamentResponseDTO> createTournament(
            @RequestParam Long organizerId,
            @RequestBody TournamentRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(tournamentService.createTournament(organizerId, dto));
    }

    @GetMapping
    @Operation(summary = "Obtener todos los torneos")
    public ResponseEntity<List<TournamentResponseDTO>> getAllTournaments() {
        return ResponseEntity.ok(tournamentService.getAllTournaments());
    }

    @GetMapping("/finalized")
    @Operation(summary = "Obtener torneos finalizados")
    public ResponseEntity<List<TournamentResponseDTO>> getFinalizedTournaments() {
        return ResponseEntity.ok(tournamentService.getFinalizedTournaments());
    }

    @GetMapping("/{tournamentId}")
    @Operation(summary = "Obtener torneo por ID")
    public ResponseEntity<TournamentResponseDTO> getTournamentById(@PathVariable String tournamentId) {
        return ResponseEntity.ok(tournamentService.getTournamentById(tournamentId));
    }

    @PatchMapping("/{tournamentId}/start")
    @Operation(summary = "Iniciar torneo")
    public ResponseEntity<TournamentResponseDTO> startTournament(
            @RequestParam Long organizerId,
            @PathVariable Long tournamentId) {
        return ResponseEntity.ok(tournamentService.startTournament(organizerId, tournamentId));
    }

    @PatchMapping("/{tournamentId}/finish")
    @Operation(summary = "Finalizar torneo")
    public ResponseEntity<TournamentResponseDTO> finishTournament(
            @RequestParam Long organizerId,
            @PathVariable Long tournamentId) {
        return ResponseEntity.ok(tournamentService.finishTournament(organizerId, tournamentId));
    }

    @PostMapping("/{tournamentId}/config")
    @Operation(summary = "Configurar torneo")
    public ResponseEntity<TournamentConfigResponseDTO> configTournament(
            @RequestParam Long organizerId,
            @PathVariable Long tournamentId,
            @RequestBody TournamentConfigRequestDTO dto) {
        return ResponseEntity.ok(tournamentService.configTournament(organizerId, tournamentId, dto));
    }

    @PostMapping("/{tournamentId}/venues")
    @Operation(summary = "Agregar sede al torneo")
    public ResponseEntity<VenueResponseDTO> addVenue(
            @RequestParam Long organizerId,
            @PathVariable Long tournamentId,
            @RequestBody VenueRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(tournamentService.addVenue(organizerId, tournamentId, dto));
    }

    @GetMapping("/{tournamentId}/venues")
    @Operation(summary = "Obtener sedes del torneo")
    public ResponseEntity<List<VenueResponseDTO>> getVenuesByTournament(@PathVariable Long tournamentId) {
        return ResponseEntity.ok(tournamentService.getVenuesByTournament(tournamentId));
    }
}

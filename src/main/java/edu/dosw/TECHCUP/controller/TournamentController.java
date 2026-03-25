package edu.dosw.TECHCUP.controller;

import edu.dosw.TECHCUP.controller.dto.request.TournamentRequestDTO;
import edu.dosw.TECHCUP.controller.dto.response.TournamentResponseDTO;
import edu.dosw.TECHCUP.core.service.TournamentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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

    @Operation(summary = "Crear un torneo")
    @PostMapping
    public ResponseEntity<TournamentResponseDTO> createTournament(
            @RequestParam String organizerId,
            @Valid @RequestBody TournamentRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(tournamentService.createTournament(organizerId, dto));
    }

    @Operation(summary = "Consultar torneo por ID")
    @GetMapping("/{id}")
    public ResponseEntity<TournamentResponseDTO> getTournament(@PathVariable String id) {
        return ResponseEntity.ok(tournamentService.getTournamentById(id));
    }

    @Operation(summary = "Listar todos los torneos")
    @GetMapping
    public ResponseEntity<List<TournamentResponseDTO>> getAllTournaments() {
        return ResponseEntity.ok(tournamentService.getAllTournaments());
    }

    @Operation(summary = "Iniciar torneo: SKETCH → ACTIVE")
    @PatchMapping("/{id}/start")
    public ResponseEntity<TournamentResponseDTO> startTournament(
            @PathVariable String id,
            @RequestParam String organizerId) {
        return ResponseEntity.ok(tournamentService.startTournament(organizerId, id));
    }

    @Operation(summary = "Poner en progreso: ACTIVE → INPROGRESS")
    @PatchMapping("/{id}/progress")
    public ResponseEntity<TournamentResponseDTO> setInProgress(
            @PathVariable String id,
            @RequestParam String organizerId) {
        return ResponseEntity.ok(tournamentService.setInProgress(organizerId, id));
    }

    @Operation(summary = "Finalizar torneo: INPROGRESS → FINALIZED")
    @PatchMapping("/{id}/finish")
    public ResponseEntity<TournamentResponseDTO> finishTournament(
            @PathVariable String id,
            @RequestParam String organizerId) {
        return ResponseEntity.ok(tournamentService.finishTournament(organizerId, id));
    }

    @Operation(summary = "Configurar reglamento y fechas del torneo (sección 6.6)")
    @PatchMapping("/{id}/config")
    public ResponseEntity<TournamentResponseDTO> configTournament(
            @PathVariable String id,
            @RequestParam String organizerId){
        return ResponseEntity.ok(
                tournamentService.configTournament(organizerId, id));
    }
}

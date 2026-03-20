package edu.dosw.TECHCUP.controller;

import edu.dosw.TECHCUP.controller.dto.TournamentRequestDTO;
import edu.dosw.TECHCUP.controller.dto.TournamentResponseDTO;
import edu.dosw.TECHCUP.core.service.TournamentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/torneos")
public class TournamentController {
    private final TournamentService tournamentService;

    public TournamentController(TournamentService tournamentService) {
        this.tournamentService = tournamentService;
    }

    @PostMapping
    public ResponseEntity<TournamentResponseDTO> crearTorneo(@RequestBody TournamentRequestDTO request) {
        TournamentResponseDTO response = tournamentService.crearTorneo(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // GET /api/torneos
    @GetMapping
    public ResponseEntity<List<TournamentResponseDTO>> listarTorneos() {
        List<TournamentResponseDTO> torneos = tournamentService.listarTorneos();
        return ResponseEntity.ok(torneos);
    }

    // GET /api/torneos/{id}
    @GetMapping("/{id}")
    public ResponseEntity<TournamentResponseDTO> obtenerTorneo(@PathVariable Long id) {
        TournamentResponseDTO torneo = tournamentService.obtenerTorneoPorId(id);
        return ResponseEntity.ok(torneo);
    }

    // PATCH /api/torneos/{id}/estado
    @PatchMapping("/{id}/estado")
    public ResponseEntity<TournamentResponseDTO> cambiarEstado(
            @PathVariable Long id,
            @RequestParam String estado) {
        TournamentResponseDTO response = tournamentService.cambiarEstado(id, estado);
        return ResponseEntity.ok(response);
    }

}

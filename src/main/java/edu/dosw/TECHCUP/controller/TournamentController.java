package edu.dosw.TECHCUP.controller;

import edu.dosw.TECHCUP.controller.dto.TournamentRequestDTO;
import edu.dosw.TECHCUP.controller.dto.TournamentResponseDTO;
import edu.dosw.TECHCUP.controller.mapper.TournamentMapper;
import edu.dosw.TECHCUP.core.model.Tournament;
import edu.dosw.TECHCUP.core.service.TournamentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/torneos")
public class TournamentController {

    private final TournamentService tournamentService;
    private final TournamentMapper tournamentMapper = TournamentMapper.INSTANCE;

    public TournamentController(TournamentService tournamentService) {
        this.tournamentService = tournamentService;
    }

    @PostMapping
    public ResponseEntity<TournamentResponseDTO> crearTorneo(@RequestBody TournamentRequestDTO request) {
        Tournament tournament = tournamentService.crearTorneo(request);
        TournamentResponseDTO response = tournamentMapper.toResponseDTO(tournament);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<TournamentResponseDTO>> listarTorneos() {
        List<TournamentResponseDTO> torneos = tournamentService.listarTorneos()
                .stream()
                .map(tournamentMapper::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(torneos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TournamentResponseDTO> obtenerTorneo(@PathVariable Long id) {
        Tournament tournament = tournamentService.obtenerTorneoPorId(id);
        TournamentResponseDTO response = tournamentMapper.toResponseDTO(tournament);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<TournamentResponseDTO> cambiarEstado(
            @PathVariable Long id,
            @RequestParam String estado) {
        Tournament tournament = tournamentService.cambiarEstado(id, estado);
        TournamentResponseDTO response = tournamentMapper.toResponseDTO(tournament);
        return ResponseEntity.ok(response);
    }
}
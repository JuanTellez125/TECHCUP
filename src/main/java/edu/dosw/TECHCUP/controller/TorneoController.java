package edu.dosw.TECHCUP.controller;

import edu.dosw.TECHCUP.controller.dto.TorneoRequestDTO;
import edu.dosw.TECHCUP.controller.dto.TorneoResponseDTO;
import edu.dosw.TECHCUP.core.service.TorneoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/torneos")
public class TorneoController {
    private final TorneoService torneoService;

    public TorneoController(TorneoService torneoService) {
        this.torneoService = torneoService;
    }

    @PostMapping
    public ResponseEntity<TorneoResponseDTO> crearTorneo(@RequestBody TorneoRequestDTO request) {
        TorneoResponseDTO response = torneoService.crearTorneo(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // GET /api/torneos
    @GetMapping
    public ResponseEntity<List<TorneoResponseDTO>> listarTorneos() {
        List<TorneoResponseDTO> torneos = torneoService.listarTorneos();
        return ResponseEntity.ok(torneos);
    }

    // GET /api/torneos/{id}
    @GetMapping("/{id}")
    public ResponseEntity<TorneoResponseDTO> obtenerTorneo(@PathVariable Long id) {
        TorneoResponseDTO torneo = torneoService.obtenerTorneoPorId(id);
        return ResponseEntity.ok(torneo);
    }

    // PATCH /api/torneos/{id}/estado
    @PatchMapping("/{id}/estado")
    public ResponseEntity<TorneoResponseDTO> cambiarEstado(
            @PathVariable Long id,
            @RequestParam String estado) {
        TorneoResponseDTO response = torneoService.cambiarEstado(id, estado);
        return ResponseEntity.ok(response);
    }

}

package edu.dosw.TECHCUP.controller;

import edu.dosw.TECHCUP.controller.dto.request.LineUpRequestDTO;
import edu.dosw.TECHCUP.controller.dto.response.LineUpResponseDTO;
import edu.dosw.TECHCUP.core.service.LineUpService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/lineups")
@RequiredArgsConstructor
@Tag(name = "LineUp", description = "Gestión de alineaciones")
public class LineUpController {

    private final LineUpService lineUpService;

    @PostMapping
    @Operation(summary = "Guardar alineación (solo capitán del equipo)")
    public ResponseEntity<LineUpResponseDTO> saveLineUp(
            @RequestParam Long captainId,
            @RequestBody LineUpRequestDTO dto) {
        return ResponseEntity.ok(lineUpService.saveLineUp(captainId, dto));
    }

    @GetMapping
    @Operation(summary = "Obtener alineación por equipo y partido")
    public ResponseEntity<LineUpResponseDTO> getLineUp(
            @RequestParam Long teamId,
            @RequestParam Long matchId) {
        return ResponseEntity.ok(lineUpService.getLineUp(teamId, matchId));
    }
}

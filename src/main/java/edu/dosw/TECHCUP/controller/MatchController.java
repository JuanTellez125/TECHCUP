package edu.dosw.TECHCUP.controller;

import edu.dosw.TECHCUP.controller.dto.response.MatchResponseDTO;
import edu.dosw.TECHCUP.core.service.MatchService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/matches")
@RequiredArgsConstructor
public class MatchController {

    private final MatchService matchService;

    @Operation(summary = "Consultar partidos asignados a un arbitro")
    @GetMapping("/referee/{refereeId}")
    public ResponseEntity<List<MatchResponseDTO>> getMatchesByReferee (@PathVariable String refereeId){
        return ResponseEntity.ok(matchService.getMatchesByReferee(refereeId));
    }
}
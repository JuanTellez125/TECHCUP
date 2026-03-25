package edu.dosw.TECHCUP.controller;

import edu.dosw.TECHCUP.controller.dto.request.TeamRequestDTO;
import edu.dosw.TECHCUP.controller.dto.response.TeamResponseDTO;
import edu.dosw.TECHCUP.controller.dto.response.UserResponseDTO;
import edu.dosw.TECHCUP.core.model.enums.Position;
import edu.dosw.TECHCUP.core.service.TeamService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teams")
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;

    @PostMapping
    public ResponseEntity<TeamResponseDTO> createTeam(
            @RequestParam String captainId,
            @Valid @RequestBody TeamRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(teamService.createTeam(captainId, dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TeamResponseDTO> getTeam(@PathVariable String id) {
        return ResponseEntity.ok(teamService.getTeamById(id));
    }

    @GetMapping("/tournament/{tournamentId}")
    public ResponseEntity<List<TeamResponseDTO>> getTeamsByTournament(
            @PathVariable String tournamentId) {
        return ResponseEntity.ok(teamService.getTeamsByTournament(tournamentId));
    }

    @PostMapping("/{teamId}/invite")
    public ResponseEntity<TeamResponseDTO> invitePlayer(
            @PathVariable String teamId,
            @RequestParam String captainId,
            @RequestParam String playerId) {
        return ResponseEntity.ok(teamService.invitePlayer(captainId, teamId, playerId));
    }

    @GetMapping("/players/search")
    public ResponseEntity<List<UserResponseDTO>> searchPlayers(
            @RequestParam(required = false) Position position,
            @RequestParam(required = false) String name) {
        return ResponseEntity.ok(teamService.searchAvailablePlayers(position, name));
    }
}

package edu.dosw.TECHCUP.controller;

import edu.dosw.TECHCUP.controller.dto.request.TeamRequestDTO;
import edu.dosw.TECHCUP.controller.dto.response.TeamResponseDTO;
import edu.dosw.TECHCUP.controller.dto.response.UserResponseDTO;
import edu.dosw.TECHCUP.core.model.enums.Position;
import edu.dosw.TECHCUP.core.service.TeamService;
import io.swagger.v3.oas.annotations.Operation;
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

    @Operation(summary = "Create team")
    @PostMapping
    public ResponseEntity<TeamResponseDTO> createTeam(
            @RequestParam String captainId,
            @Valid @RequestBody TeamRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(teamService.createTeam(captainId, dto));
    }

    @Operation(summary = "Get team by ID")
    @GetMapping("/{id}")
    public ResponseEntity<TeamResponseDTO> getTeam(@PathVariable String id) {
        return ResponseEntity.ok(teamService.getTeamById(id));
    }

    @Operation(summary = "List teams in a tournament")
    @GetMapping("/tournament/{tournamentId}")
    public ResponseEntity<List<TeamResponseDTO>> getTeamsByTournament(
            @PathVariable String tournamentId) {
        return ResponseEntity.ok(teamService.getTeamsByTournament(tournamentId));
    }

    @Operation(summary = "Invite player to team")
    @PostMapping("/{teamId}/invite")
    public ResponseEntity<TeamResponseDTO> invitePlayer(
            @PathVariable String teamId,
            @RequestParam String captainId,
            @RequestParam String playerId) {
        return ResponseEntity.ok(teamService.invitePlayer(captainId, teamId, playerId));
    }

    @Operation(summary = "Search for available players by position and name")
    @GetMapping("/players/search")
    public ResponseEntity<List<UserResponseDTO>> searchPlayers(
            @RequestParam(required = false) Position position,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String gender,
            @RequestParam(required = false) String identification,
            @RequestParam(required = false) Integer semester) {
        return ResponseEntity.ok(
                teamService.searchAvailablePlayers(position, name, gender, identification, semester));
    }
}

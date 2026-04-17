package edu.dosw.TECHCUP.controller;

import edu.dosw.TECHCUP.controller.dto.request.TeamRequestDTO;
import edu.dosw.TECHCUP.controller.dto.response.InvitationResponseDTO;
import edu.dosw.TECHCUP.controller.dto.response.SportProfileResponseDTO;
import edu.dosw.TECHCUP.controller.dto.response.TeamResponseDTO;
import edu.dosw.TECHCUP.controller.mapper.InvitationMapper;
import edu.dosw.TECHCUP.controller.mapper.SportProfileMapper;
import edu.dosw.TECHCUP.controller.mapper.TeamMapper;
import edu.dosw.TECHCUP.core.model.enums.Position;
import edu.dosw.TECHCUP.core.service.TeamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/teams")
@RequiredArgsConstructor
@Tag(name = "Team", description = "Team management")
public class TeamController {

    private final TeamService teamService;
    private final TeamMapper teamMapper;
    private final InvitationMapper invitationMapper;
    private final SportProfileMapper sportProfileMapper;

    @PostMapping
    @PreAuthorize("hasAnyRole('CAPTAIN', 'ADMINISTRATOR')")
    @Operation(summary = "Create team (captain only)")
    public ResponseEntity<TeamResponseDTO> createTeam(
            @RequestParam Long captainId,
            @RequestBody TeamRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(teamMapper.toDto(teamService.createTeam(captainId, teamMapper.toModel(dto))));
    }

    @GetMapping("/{teamId}")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'ORGANIZER', 'REFEREE', 'CAPTAIN', 'PLAYER')")
    @Operation(summary = "Get team by ID")
    public ResponseEntity<TeamResponseDTO> getTeamById(@PathVariable Long teamId) {
        return ResponseEntity.ok(teamMapper.toDto(teamService.getTeamById(teamId)));
    }

    @GetMapping("/captain/{captainId}")
    @PreAuthorize("hasAnyRole('CAPTAIN', 'ADMINISTRATOR')")
    @Operation(summary = "Get team by captain ID")
    public ResponseEntity<TeamResponseDTO> getTeamByCaptain(@PathVariable Long captainId) {
        return ResponseEntity.ok(teamMapper.toDto(teamService.getTeamByCaptain(captainId)));
    }

    @GetMapping("/tournament/{tournamentId}")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'ORGANIZER', 'REFEREE', 'CAPTAIN', 'PLAYER')")
    @Operation(summary = "Get teams by tournament")
    public ResponseEntity<List<TeamResponseDTO>> getTeamsByTournament(@PathVariable Long tournamentId) {
        return ResponseEntity.ok(
                teamService.getTeamsByTournament(tournamentId).stream()
                        .map(teamMapper::toDto)
                        .collect(Collectors.toList())
        );
    }

    @PostMapping("/{teamId}/invite")
    @PreAuthorize("hasAnyRole('CAPTAIN', 'ADMINISTRATOR')")
    @Operation(summary = "Invite player to team")
    public ResponseEntity<InvitationResponseDTO> invitePlayer(
            @RequestParam Long captainId,
            @PathVariable Long teamId,
            @RequestParam Long playerId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(invitationMapper.toDto(teamService.invitePlayer(captainId, teamId, playerId)));
    }

    @PatchMapping("/invitations/{invitationId}/respond")
    @PreAuthorize("hasAnyRole('PLAYER', 'ADMINISTRATOR')")
    @Operation(summary = "Respond to invitation (accept/reject)")
    public ResponseEntity<InvitationResponseDTO> respondInvitation(
            @RequestParam Long playerId,
            @PathVariable Long invitationId,
            @RequestParam boolean accept) {
        return ResponseEntity.ok(
                invitationMapper.toDto(teamService.respondInvitation(playerId, invitationId, accept))
        );
    }

    @GetMapping("/invitations/player/{playerId}")
    @PreAuthorize("hasAnyRole('PLAYER', 'CAPTAIN', 'ADMINISTRATOR')")
    @Operation(summary = "Get invitations from a player")
    public ResponseEntity<List<InvitationResponseDTO>> getInvitationsByPlayer(@PathVariable Long playerId) {
        return ResponseEntity.ok(
                teamService.getInvitationsByPlayer(playerId).stream()
                        .map(invitationMapper::toDto)
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("/players/available")
    @PreAuthorize("hasAnyRole('CAPTAIN', 'ADMINISTRATOR', 'ORGANIZER')")
    @Operation(summary = "Buscar jugadores disponibles")
    public ResponseEntity<List<SportProfileResponseDTO>> searchAvailablePlayers(
            @RequestParam(required = false) Position position,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String gender,
            @RequestParam(required = false) String identification,
            @RequestParam(required = false) Integer semester) {
        return ResponseEntity.ok(
                teamService.searchAvailablePlayers(position, name, gender, identification, semester).stream()
                        .map(sportProfileMapper::toDto)
                        .collect(Collectors.toList())
        );
    }

    @PostMapping("/{teamId}/validate")
    @PreAuthorize("hasAnyRole('CAPTAIN', 'ADMINISTRATOR')")
    @Operation(summary = "Validate team size")
    public ResponseEntity<Void> validateTeam(@PathVariable Long teamId) {
        teamService.validateTeam(teamId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{teamId}/members/{playerId}")
    @PreAuthorize("hasAnyRole('CAPTAIN', 'ADMINISTRATOR')")
    @Operation(summary = "Remove player from team")
    public ResponseEntity<Void> removePlayer(
            @RequestParam Long captainId,
            @PathVariable Long teamId,
            @PathVariable Long playerId) {
        teamService.removePlayer(captainId, teamId, playerId);
        return ResponseEntity.noContent().build();
    }
}

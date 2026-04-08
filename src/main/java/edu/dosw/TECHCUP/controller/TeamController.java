package edu.dosw.TECHCUP.controller;

import edu.dosw.TECHCUP.controller.dto.request.TeamRequestDTO;
import edu.dosw.TECHCUP.controller.dto.response.InvitationResponseDTO;
import edu.dosw.TECHCUP.controller.dto.response.SportProfileResponseDTO;
import edu.dosw.TECHCUP.controller.dto.response.TeamResponseDTO;
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

@RestController
@RequestMapping("/api/teams")
@RequiredArgsConstructor
@Tag(name = "Team", description = "Gestión de equipos")
public class TeamController {

    private final TeamService teamService;

    @PostMapping
    @PreAuthorize("hasAnyRole('CAPTAIN', 'ADMINISTRATOR')")
    @Operation(summary = "Crear equipo (solo capitán)")
    public ResponseEntity<TeamResponseDTO> createTeam(
            @RequestParam Long captainId,
            @RequestBody TeamRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(teamService.createTeam(captainId, dto));
    }

    @GetMapping("/{teamId}")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'ORGANIZER', 'REFEREE', 'CAPTAIN', 'PLAYER')")
    @Operation(summary = "Obtener equipo por ID")
    public ResponseEntity<TeamResponseDTO> getTeamById(@PathVariable Long teamId) {
        return ResponseEntity.ok(teamService.getTeamById(teamId));
    }

    @GetMapping("/tournament/{tournamentId}")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'ORGANIZER', 'REFEREE', 'CAPTAIN', 'PLAYER')")
    @Operation(summary = "Obtener equipos por torneo")
    public ResponseEntity<List<TeamResponseDTO>> getTeamsByTournament(@PathVariable Long tournamentId) {
        return ResponseEntity.ok(teamService.getTeamsByTournament(tournamentId));
    }

    @PostMapping("/{teamId}/invite")
    @PreAuthorize("hasAnyRole('CAPTAIN', 'ADMINISTRATOR')")
    @Operation(summary = "Invitar jugador al equipo")
    public ResponseEntity<InvitationResponseDTO> invitePlayer(
            @RequestParam Long captainId,
            @PathVariable Long teamId,
            @RequestParam Long playerId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(teamService.invitePlayer(captainId, teamId, playerId));
    }

    @PatchMapping("/invitations/{invitationId}/respond")
    @PreAuthorize("hasAnyRole('PLAYER', 'ADMINISTRATOR')")
    @Operation(summary = "Responder invitación (aceptar/rechazar)")
    public ResponseEntity<InvitationResponseDTO> respondInvitation(
            @RequestParam Long playerId,
            @PathVariable Long invitationId,
            @RequestParam boolean accept) {
        return ResponseEntity.ok(teamService.respondInvitation(playerId, invitationId, accept));
    }

    @GetMapping("/invitations/player/{playerId}")
    @PreAuthorize("hasAnyRole('PLAYER', 'CAPTAIN', 'ADMINISTRATOR')")
    @Operation(summary = "Obtener invitaciones de un jugador")
    public ResponseEntity<List<InvitationResponseDTO>> getInvitationsByPlayer(@PathVariable Long playerId) {
        return ResponseEntity.ok(teamService.getInvitationsByPlayer(playerId));
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
        return ResponseEntity.ok(teamService.searchAvailablePlayers(position, name, gender, identification, semester));
    }

    @PostMapping("/{teamId}/validate")
    @PreAuthorize("hasAnyRole('CAPTAIN', 'ADMINISTRATOR')")
    @Operation(summary = "Validar tamaño del equipo")
    public ResponseEntity<Void> validateTeam(@PathVariable Long teamId) {
        teamService.validateTeam(teamId);
        return ResponseEntity.ok().build();
    }
}

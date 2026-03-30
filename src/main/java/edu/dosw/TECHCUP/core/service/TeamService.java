package edu.dosw.TECHCUP.core.service;

import edu.dosw.TECHCUP.controller.dto.request.TeamRequestDTO;
import edu.dosw.TECHCUP.controller.dto.response.InvitationResponseDTO;
import edu.dosw.TECHCUP.controller.dto.response.SportProfileResponseDTO;
import edu.dosw.TECHCUP.controller.dto.response.TeamResponseDTO;
import edu.dosw.TECHCUP.controller.dto.response.UserResponseDTO;
import edu.dosw.TECHCUP.controller.mapper.InvitationMapper;
import edu.dosw.TECHCUP.controller.mapper.SportProfileMapper;
import edu.dosw.TECHCUP.controller.mapper.TeamMapper;
import edu.dosw.TECHCUP.controller.mapper.UserMapper;
import edu.dosw.TECHCUP.core.exception.UserNotFoundException;
import edu.dosw.TECHCUP.core.exception.UserValidationException;
import edu.dosw.TECHCUP.core.model.*;
import edu.dosw.TECHCUP.core.model.enums.*;
import edu.dosw.TECHCUP.persistence.repository.TeamRepository;
import edu.dosw.TECHCUP.persistence.repository.UserRepository;
import edu.dosw.TECHCUP.core.util.IdGeneratorUtil;
import edu.dosw.TECHCUP.core.validator.TeamValidator;
import edu.dosw.TECHCUP.persistence.repository.InvitationRepository;
import edu.dosw.TECHCUP.persistence.repository.SportProfileRepository;
import edu.dosw.TECHCUP.persistence.repository.TeamMemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final InvitationRepository invitationRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final SportProfileRepository sportProfileRepository;
    private final TeamMapper teamMapper;
    private final InvitationMapper invitationMapper;
    private final SportProfileMapper sportProfileMapper;
    private final TeamValidator teamValidator;

    @Transactional
    public TeamResponseDTO createTeam(Long captainId, TeamRequestDTO dto) {
        User captain = userRepository.findById(captainId)
                .orElseThrow(() -> new UserNotFoundException(captainId));

        teamValidator.validateCaptainRole(captain);
        teamValidator.validateTeamName(dto.getName());

        if (teamRepository.existsByName(dto.getName()))
            throw new UserValidationException("Ya existe un equipo con el nombre: " + dto.getName());

        if (teamMemberRepository.existsByTeam_IdAndUser_User_id(null, captainId))
            throw new UserValidationException("El capitán ya pertenece a otro equipo.");

        Team team = Team.builder()
                .captain(captain)
                .name(dto.getName())
                .shieldUrl(dto.getShieldUrl())
                .active(true)
                .build();

        Team saved = teamRepository.save(team);

        TeamMember captainMember = TeamMember.builder()
                .team(saved)
                .user(captain)
                .status(TeamMemberStatus.ACEPTADO)
                .available(true)
                .build();
        teamMemberRepository.save(captainMember);

        log.info("Equipo {} creado por capitán {}", saved.getName(), captainId);
        return teamMapper.toDto(saved);
    }

    public TeamResponseDTO getTeamById(Long teamId) {
        Team team = teamRepository.findById(String.valueOf(teamId))
                .orElseThrow(() -> new UserNotFoundException(teamId));
        return teamMapper.toDto(team);
    }

    @Transactional
    public InvitationResponseDTO invitePlayer(Long captainId, Long teamId, Long playerId) {
        User captain = userRepository.findById(captainId)
                .orElseThrow(() -> new UserNotFoundException(captainId));
        teamValidator.validateCaptainRole(captain);

        Team team = teamRepository.findById(String.valueOf(teamId))
                .orElseThrow(() -> new UserNotFoundException(teamId));

        if (!team.getCaptain().getUser_id().equals(captainId))
            throw new UserValidationException("No eres el capitán de este equipo.");

        User player = userRepository.findById(playerId)
                .orElseThrow(() -> new UserNotFoundException(playerId));

        if (teamMemberRepository.existsByTeam_IdAndUser_User_id(teamId, playerId))
            throw new UserValidationException("El jugador ya pertenece a este equipo.");

        long currentMembers = teamMemberRepository.findAllByTeam_Id(teamId).size();
        if (currentMembers >= 12)
            throw new UserValidationException("El equipo ya tiene el máximo de 12 jugadores.");

        Invitation invitation = Invitation.builder()
                .team(team)
                .invitedUser(player)
                .invitedBy(captain)
                .status(InvitationStatus.PENDIENTE)
                .build();

        Invitation saved = invitationRepository.save(invitation);
        log.info("Invitación enviada al jugador {} para el equipo {}", playerId, teamId);
        return invitationMapper.toDto(saved);
    }

    @Transactional
    public InvitationResponseDTO respondInvitation(Long playerId, Long invitationId, boolean accept) {
        Invitation invitation = invitationRepository.findById(invitationId)
                .orElseThrow(() -> new UserNotFoundException(invitationId));

        if (!invitation.getInvitedUser().getUser_id().equals(playerId))
            throw new UserValidationException("Esta invitación no es para ti.");

        if (invitation.getStatus() != InvitationStatus.PENDIENTE)
            throw new UserValidationException("Esta invitación ya fue respondida.");

        if (accept) {
            invitation.setStatus(InvitationStatus.ACEPTADA);
            TeamMember member = TeamMember.builder()
                    .team(invitation.getTeam())
                    .user(invitation.getInvitedUser())
                    .status(TeamMemberStatus.ACEPTADO)
                    .available(true)
                    .build();
            teamMemberRepository.save(member);
            log.info("Jugador {} aceptó unirse al equipo {}", playerId, invitation.getTeam().getId());
        } else {
            invitation.setStatus(InvitationStatus.RECHAZADA);
        }

        return invitationMapper.toDto(invitationRepository.save(invitation));
    }

    public List<SportProfileResponseDTO> searchAvailablePlayers(Position position, String name,
                                                                String gender, String identification,
                                                                Integer semester) {
        List<SportProfile> profiles = position != null
                ? sportProfileRepository.findAllByPrimaryPositionAndAvailableTrue(position)
                : sportProfileRepository.findAllByAvailableTrue();

        if (name != null && !name.isBlank()) {
            String lower = name.toLowerCase();
            profiles = profiles.stream()
                    .filter(p -> p.getUser().getFirstName().toLowerCase().contains(lower)
                            || p.getUser().getLastName().toLowerCase().contains(lower))
                    .collect(Collectors.toList());
        }

        return profiles.stream().map(sportProfileMapper::toDto).collect(Collectors.toList());
    }

    public void validateTeam(Long teamId) {
        List<TeamMember> members = teamMemberRepository.findAllByTeam_Id(teamId);
        teamValidator.validateTeamSize(members.stream()
                .map(TeamMember::getUser).collect(Collectors.toList()));
    }

    public List<InvitationResponseDTO> getInvitationsByPlayer(Long playerId) {
        return invitationRepository.findAllByInvitedUser_User_id(playerId)
                .stream().map(invitationMapper::toDto).collect(Collectors.toList());
    }

    public List<TeamResponseDTO> getTeamsByTournament(String tournamentId) {
        return teamRepository.findAllByTournamentId(tournamentId)
                .stream()
                .map(teamMapper::toDto)
                .collect(Collectors.toList());
    }
}

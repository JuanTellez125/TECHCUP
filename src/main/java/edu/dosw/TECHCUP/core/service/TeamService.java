package edu.dosw.TECHCUP.core.service;

import edu.dosw.TECHCUP.core.exception.TeamNotFoundException;
import edu.dosw.TECHCUP.core.exception.UserNotFoundException;
import edu.dosw.TECHCUP.core.exception.UserValidationException;
import edu.dosw.TECHCUP.core.model.*;
import edu.dosw.TECHCUP.core.model.enums.*;
import edu.dosw.TECHCUP.persistence.entity.*;
import edu.dosw.TECHCUP.persistence.mapper.InvitationPersistenceMapper;
import edu.dosw.TECHCUP.persistence.mapper.SportProfilePersistenceMapper;
import edu.dosw.TECHCUP.persistence.mapper.TeamPersistenceMapper;
import edu.dosw.TECHCUP.persistence.repository.TeamRepository;
import edu.dosw.TECHCUP.persistence.repository.UserRepository;
import edu.dosw.TECHCUP.core.util.IdGeneratorUtil;
import edu.dosw.TECHCUP.core.validator.TeamValidator;
import edu.dosw.TECHCUP.persistence.repository.InvitationRepository;
import edu.dosw.TECHCUP.persistence.repository.SportProfileRepository;
import edu.dosw.TECHCUP.persistence.repository.TeamMemberRepository;
import edu.dosw.TECHCUP.persistence.repository.TournamentRegistrationRepository;
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
    private final TournamentRegistrationRepository tournamentRegistrationRepository;
    private final TeamValidator teamValidator;
    private final TeamPersistenceMapper teamPersistenceMapper;
    private final InvitationPersistenceMapper invitationPersistenceMapper;
    private final SportProfilePersistenceMapper sportProfilePersistenceMapper;

    @Transactional
    public Team createTeam(Long captainId, Team team) {
        UserEntity captain = userRepository.findById(captainId)
                .orElseThrow(() -> new UserNotFoundException(captainId));

        teamValidator.validateCaptainRole(captain);
        teamValidator.validateTeamName(team.getName());

        if (teamRepository.existsByName(team.getName()))
            throw new UserValidationException("There is already a team with the name: " + team.getName());

        if (teamMemberRepository.existsByTeam_IdAndUser_User_id(null, captainId))
            throw new UserValidationException("The captain already belongs to another team.");

        TeamEntity teamEntity = TeamEntity.builder()
                .captain(captain)
                .name(team.getName())
                .shieldUrl(team.getShieldUrl())
                .active(true)
                .build();

        TeamEntity saved = teamRepository.save(teamEntity);

        TeamMemberEntity captainMember = TeamMemberEntity.builder()
                .team(saved)
                .user(captain)
                .status(TeamMemberStatus.ACEPTADO)
                .available(true)
                .build();
        teamMemberRepository.save(captainMember);

        log.info("Team {} created by captain {}", saved.getName(), captainId);
        return teamPersistenceMapper.toModel(saved);
    }

    public Team getTeamById(Long teamId) {
        TeamEntity team = teamRepository.findById(teamId)
                .orElseThrow(() -> new UserNotFoundException(teamId));
        return teamPersistenceMapper.toModel(team);
    }

    @Transactional
    public Invitation invitePlayer(Long captainId, Long teamId, Long playerId) {
        UserEntity captain = userRepository.findById(captainId)
                .orElseThrow(() -> new UserNotFoundException(captainId));
        teamValidator.validateCaptainRole(captain);

        TeamEntity team = teamRepository.findById(teamId)
                .orElseThrow(() -> new UserNotFoundException(teamId));

        if (!team.getCaptain().getUser_id().equals(captainId))
            throw new UserValidationException("You are not the captain of this team.");

        UserEntity player = userRepository.findById(playerId)
                .orElseThrow(() -> new UserNotFoundException(playerId));

        if (teamMemberRepository.existsByTeam_IdAndUser_User_id(teamId, playerId))
            throw new UserValidationException("The player already belongs to this team.");

        long currentMembers = teamMemberRepository.findAllByTeam_Id(teamId).size();
        if (currentMembers >= 12)
            throw new UserValidationException("The team already has the maximum of 12 players.");

        InvitationEntity invitation = InvitationEntity.builder()
                .team(team)
                .invitedUser(player)
                .invitedBy(captain)
                .status(InvitationStatus.PENDIENTE)
                .build();

        InvitationEntity saved = invitationRepository.save(invitation);
        log.info("Invitation sent to player {} for the team {}", playerId, teamId);
        return invitationPersistenceMapper.toModel(saved);
    }

    @Transactional
    public Invitation respondInvitation(Long playerId, Long invitationId, boolean accept) {
        InvitationEntity invitation = invitationRepository.findById(invitationId)
                .orElseThrow(() -> new UserNotFoundException(invitationId));

        if (!invitation.getInvitedUser().getUser_id().equals(playerId))
            throw new UserValidationException("This invitation is not for you.");

        if (invitation.getStatus() != InvitationStatus.PENDIENTE)
            throw new UserValidationException("This invitation has already been answered.");

        if (accept) {
            invitation.setStatus(InvitationStatus.ACEPTADA);
            TeamMemberEntity member = TeamMemberEntity.builder()
                    .team(invitation.getTeam())
                    .user(invitation.getInvitedUser())
                    .status(TeamMemberStatus.ACEPTADO)
                    .available(true)
                    .build();
            teamMemberRepository.save(member);
            log.info("Player {} agreed to join the team {}", playerId, invitation.getTeam().getId());
        } else {
            invitation.setStatus(InvitationStatus.RECHAZADA);
        }

        return invitationPersistenceMapper.toModel(invitationRepository.save(invitation));
    }

    public List<SportProfile> searchAvailablePlayers(Position position, String name,
                                                      String gender, String identification,
                                                      Integer semester) {
        List<SportProfileEntity> profiles = position != null
                ? sportProfileRepository.findAllByPrimaryPositionAndAvailableTrue(position)
                : sportProfileRepository.findAllByAvailableTrue();

        if (name != null && !name.isBlank()) {
            String lower = name.toLowerCase();
            profiles = profiles.stream()
                    .filter(p -> p.getUser().getFirstName().toLowerCase().contains(lower)
                            || p.getUser().getLastName().toLowerCase().contains(lower))
                    .collect(Collectors.toList());
        }

        if (semester != null && position != null) {
            profiles = profiles.stream()
                    .filter(p -> semester.equals(p.getSemester()))
                    .collect(Collectors.toList());
        }

        return profiles.stream()
                .map(sportProfilePersistenceMapper::toModel)
                .collect(Collectors.toList());
    }

    public void validateTeam(Long teamId) {
        List<TeamMemberEntity> members = teamMemberRepository.findAllByTeam_Id(teamId);
        teamValidator.validateTeamSize(members.stream()
                .map(TeamMemberEntity::getUser).collect(Collectors.toList()));
    }

    public List<Invitation> getInvitationsByPlayer(Long playerId) {
        return invitationRepository.findAllByInvitedUser_User_id(playerId)
                .stream()
                .map(invitationPersistenceMapper::toModel)
                .collect(Collectors.toList());
    }

    public List<Team> getTeamsByTournament(Long tournamentId) {
        return tournamentRegistrationRepository.findAllByTournament_Tournament_id(tournamentId)
                .stream()
                .map(r -> teamPersistenceMapper.toModel(r.getTeam()))
                .collect(Collectors.toList());
    }

    @Transactional
    public void removePlayer(Long captainId, Long teamId, Long playerId) {
        UserEntity captain = userRepository.findById(captainId)
                .orElseThrow(() -> new UserNotFoundException(captainId));
        if (captain.getUserType() != Role.CAPTAIN)
            throw new UserValidationException("Only the captain can remove players");

        TeamEntity team = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamNotFoundException(teamId));
        if (!team.getCaptain().getUser_id().equals(captainId))
            throw new UserValidationException("Only the captain can remove players");

        List<TournamentRegistrationEntity> activeRegistrations =
                tournamentRegistrationRepository.findActiveOrFinalizedByTeam_Id(teamId);
        if (!activeRegistrations.isEmpty())
            throw new UserValidationException("No roster changes can be made during the tournament");

        if (captainId.equals(playerId))
            throw new UserValidationException("The team captain cannot be removed");

        TeamMemberEntity member = teamMemberRepository.findByTeam_IdAndUser_User_id(teamId, playerId)
                .orElseThrow(() -> new UserNotFoundException(playerId));
        if (member.getStatus() != TeamMemberStatus.ACEPTADO)
            throw new UserNotFoundException(playerId);

        member.setStatus(TeamMemberStatus.REMOVIDO);
        teamMemberRepository.save(member);

        long activeCount = teamMemberRepository.findAllByTeam_Id(teamId).stream()
                .filter(m -> m.getStatus() == TeamMemberStatus.ACEPTADO)
                .count();
        if (activeCount < 7) {
            log.warn("Team {} now has {} active players, below the minimum of 7", teamId, activeCount);
        }

        log.info("Player {} removed from team {} by captain {}", playerId, teamId, captainId);
    }
}

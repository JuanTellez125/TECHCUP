package edu.dosw.TECHCUP.core.service;

import edu.dosw.TECHCUP.controller.dto.request.LineUpEntryDTO;
import edu.dosw.TECHCUP.controller.dto.request.LineUpRequestDTO;
import edu.dosw.TECHCUP.core.model.LineUp;
import edu.dosw.TECHCUP.core.exception.TeamNotFoundException;
import edu.dosw.TECHCUP.core.exception.UserNotFoundException;
import edu.dosw.TECHCUP.core.exception.UserValidationException;
import edu.dosw.TECHCUP.core.model.enums.Role;
import edu.dosw.TECHCUP.core.model.enums.TeamMemberStatus;
import edu.dosw.TECHCUP.persistence.entity.LineUpEntity;
import edu.dosw.TECHCUP.persistence.entity.MatchEntity;
import edu.dosw.TECHCUP.persistence.entity.TeamEntity;
import edu.dosw.TECHCUP.persistence.entity.UserEntity;
import edu.dosw.TECHCUP.persistence.mapper.LineUpPersistenceMapper;
import edu.dosw.TECHCUP.persistence.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class LineUpService {

    private final LineUpRepository lineUpRepository;
    private final LineUpPersistenceMapper lineUpPersistenceMapper;
    private final TeamRepository teamRepository;
    private final MatchRepository matchRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final UserRepository userRepository;

    @Transactional
    public List<LineUp> saveLineUp(Long captainId, Long matchId, LineUpRequestDTO dto) {
        UserEntity captain = userRepository.findById(captainId)
                .orElseThrow(() -> new UserNotFoundException(captainId));
        if (captain.getUserType() != Role.CAPTAIN)
            throw new UserValidationException("Only the captain can define the lineup.");

        MatchEntity match = matchRepository.findById(matchId)
                .orElseThrow(() -> new UserNotFoundException(matchId));

        TeamEntity team = teamRepository.findById(dto.getTeamId())
                .orElseThrow(() -> new TeamNotFoundException(dto.getTeamId()));

        if (!team.getCaptain().getUser_id().equals(captainId))
            throw new UserValidationException("Only the captain of this team can define its lineup.");

        boolean participates = match.getTeam1().getId().equals(team.getId())
                || match.getTeam2().getId().equals(team.getId());
        if (!participates)
            throw new UserValidationException("El equipo no participa en este partido");

        if (dto.getPlayers() == null || dto.getPlayers().isEmpty())
            throw new UserValidationException("La alineación no puede estar vacía");

        for (LineUpEntryDTO entry : dto.getPlayers()) {
            UserEntity player = userRepository.findById(entry.getUserId())
                    .orElseThrow(() -> new UserNotFoundException(entry.getUserId()));

            teamMemberRepository.findByTeam_IdAndUser_User_id(team.getId(), player.getUser_id())
                    .filter(m -> m.getStatus() == TeamMemberStatus.ACEPTADO)
                    .orElseThrow(() -> new UserValidationException(
                            "El jugador " + entry.getUserId() + " no es miembro activo del equipo"));
        }

        long distinctJerseys = dto.getPlayers().stream()
                .map(LineUpEntryDTO::getJerseyNumber)
                .distinct()
                .count();
        if (distinctJerseys < dto.getPlayers().size())
            throw new UserValidationException("No puede haber dos titulares con el mismo número de dorsal");

        List<LineUpEntity> existing = lineUpRepository
                .findAllByMatch_Match_idAndTeam_Id(matchId, team.getId());
        if (!existing.isEmpty())
            lineUpRepository.deleteAll(existing);

        List<LineUpEntity> saved = dto.getPlayers().stream()
                .map(entry -> {
                    UserEntity player = userRepository.findById(entry.getUserId())
                            .orElseThrow(() -> new UserNotFoundException(entry.getUserId()));
                    return LineUpEntity.builder()
                            .match(match)
                            .team(team)
                            .user(player)
                            .role(entry.getRole())
                            .position(entry.getPosition())
                            .jerseyNumber(entry.getJerseyNumber())
                            .build();
                })
                .collect(Collectors.toList());

        List<LineUpEntity> savedAll = lineUpRepository.saveAll(saved);
        log.info("Lineup of {} players saved for team {} in match {} by captain {}",
                savedAll.size(), team.getId(), matchId, captainId);

        return savedAll.stream()
                .map(lineUpPersistenceMapper::toModel)
                .collect(Collectors.toList());
    }

    public List<LineUp> getLineUp(Long matchId, Long teamId) {
        matchRepository.findById(matchId)
                .orElseThrow(() -> new UserNotFoundException(matchId));
        teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamNotFoundException(teamId));

        return lineUpRepository.findAllByMatch_Match_idAndTeam_Id(matchId, teamId)
                .stream()
                .map(lineUpPersistenceMapper::toModel)
                .collect(Collectors.toList());
    }
}
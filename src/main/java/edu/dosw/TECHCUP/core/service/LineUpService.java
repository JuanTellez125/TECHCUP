package edu.dosw.TECHCUP.core.service;

import edu.dosw.TECHCUP.controller.dto.request.LineUpRequestDTO;
import edu.dosw.TECHCUP.controller.dto.response.LineUpResponseDTO;
import edu.dosw.TECHCUP.controller.mapper.LineUpMapper;
import edu.dosw.TECHCUP.core.exception.TeamNotFoundException;
import edu.dosw.TECHCUP.core.exception.UserNotFoundException;
import edu.dosw.TECHCUP.core.exception.UserValidationException;
import edu.dosw.TECHCUP.core.model.LineUp;
import edu.dosw.TECHCUP.core.model.Match;
import edu.dosw.TECHCUP.core.model.Team;
import edu.dosw.TECHCUP.core.model.User;
import edu.dosw.TECHCUP.core.model.enums.Role;
import edu.dosw.TECHCUP.persistence.entity.LineUpEntity;
import edu.dosw.TECHCUP.persistence.entity.MatchEntity;
import edu.dosw.TECHCUP.persistence.entity.TeamEntity;
import edu.dosw.TECHCUP.persistence.entity.UserEntity;
import edu.dosw.TECHCUP.persistence.repository.*;
import edu.dosw.TECHCUP.core.util.IdGeneratorUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class LineUpService {

    private final LineUpRepository lineUpRepository;
    private final LineUpMapper lineUpMapper;
    private final TeamRepository teamRepository;
    private final MatchRepository matchRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final UserRepository userRepository;

    @Transactional
    public LineUpResponseDTO saveLineUp(Long captainId, LineUpRequestDTO dto) {
        UserEntity captain = userRepository.findById(captainId)
                .orElseThrow(() -> new UserNotFoundException(captainId));

        if (captain.getUserType() != Role.CAPTAIN)
            throw new UserValidationException("Only the captain can define the lineup.");

        MatchEntity match = matchRepository.findById(dto.getMatchId())
                .orElseThrow(() -> new UserNotFoundException(dto.getMatchId()));

        TeamEntity team = teamRepository.findById(dto.getTeamId())
                .orElseThrow(() -> new UserNotFoundException(dto.getTeamId()));

        if (!team.getCaptain().getUser_id().equals(captainId))
            throw new UserValidationException("Only the captain of this team can define its lineup.");


        UserEntity player = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new UserNotFoundException(dto.getUserId()));

        if (!teamMemberRepository.existsByTeam_IdAndUser_User_id(team.getId(), player.getUser_id()))
            throw new UserValidationException("The player does not belong to this team.");

        boolean alreadyRegistered = lineUpRepository
                .findAllByMatch_Match_idAndTeam_Id(dto.getMatchId(), dto.getTeamId())
                .stream()
                .anyMatch(l -> l.getUser().getUser_id().equals(player.getUser_id()));

        if (alreadyRegistered)
            throw new UserValidationException("The player is already registered in the lineup for this match.");

        LineUpEntity lineUp = LineUpEntity.builder()
                .match(match)
                .team(team)
                .user(player)
                .role(dto.getRole())
                .position(dto.getPosition())
                .jerseyNumber(dto.getJerseyNumber())
                .build();

        LineUpEntity saved = lineUpRepository.save(lineUp);
        log.info("Player {} added to team lineup {} in match {} as {}",
                player.getUser_id(), team.getId(), match.getMatch_id(), dto.getRole());
        return lineUpMapper.toDto(saved);
    }

    public LineUpResponseDTO getLineUp(Long teamId, Long matchId) {
        LineUpEntity lineUp = lineUpRepository.findByMatchAndTeam(teamId, matchId)
                .orElseThrow(() -> new TeamNotFoundException(teamId, matchId));
        return lineUpMapper.toDto(lineUp);
    }
}

package edu.dosw.TECHCUP.core.service;

import edu.dosw.TECHCUP.controller.dto.request.MatchResultRequestDTO;
import edu.dosw.TECHCUP.controller.dto.response.*;
import edu.dosw.TECHCUP.controller.mapper.MatchEventMapper;
import edu.dosw.TECHCUP.core.exception.MatchResultAlreadyExistsException;
import edu.dosw.TECHCUP.core.exception.TournamentNotFoundException;
import edu.dosw.TECHCUP.core.exception.TournamentValidationException;
import edu.dosw.TECHCUP.core.exception.UserNotFoundException;
import edu.dosw.TECHCUP.core.model.*;
import edu.dosw.TECHCUP.core.model.enums.Event;
import edu.dosw.TECHCUP.core.model.enums.MatchPhase;
import edu.dosw.TECHCUP.core.model.enums.Role;
import edu.dosw.TECHCUP.persistence.entity.*;
import edu.dosw.TECHCUP.persistence.mapper.*;
import edu.dosw.TECHCUP.persistence.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class MatchService {

    private final MatchRepository matchRepository;
    private final MatchResultRepository matchResultRepository;
    private final MatchEventRepository matchEventRepository;
    private final LineUpRepository lineUpRepository;
    private final TeamRepository teamRepository;
    private final TournamentRepository tournamentRepository;
    private final VenueRepository venueRepository;
    private final UserRepository userRepository;
    private final StandingRepository standingRepository;
    private final MatchEventMapper matchEventMapper;
    private final MatchPersistenceMapper matchPersistenceMapper;
    private final MatchResultPersistenceMapper matchResultPersistenceMapper;
    private final MatchEventPersistenceMapper matchEventPersistenceMapper;
    private final StandingPersistenceMapper standingPersistenceMapper;

    @Transactional
    public Match scheduleMatch(Match match) {
        TournamentEntity tournament = tournamentRepository.findById(match.getTournament().getTournamentId())
                .orElseThrow(() -> new TournamentNotFoundException(match.getTournament().getTournamentId()));
        TeamEntity team1 = teamRepository.findById(match.getTeam1().getId())
                .orElseThrow(() -> new UserNotFoundException(match.getTeam1().getId()));
        TeamEntity team2 = teamRepository.findById(match.getTeam2().getId())
                .orElseThrow(() -> new UserNotFoundException(match.getTeam2().getId()));
        VenueEntity venue = venueRepository.findById(match.getVenue().getVenueId())
                .orElseThrow(() -> new UserNotFoundException(match.getVenue().getVenueId()));

        if (team1.getId().equals(team2.getId()))
            throw new TournamentValidationException("The home and visiting teams cannot be the same.");

        Long refereeId = match.getReferee() != null ? match.getReferee().getUserId() : null;
        UserEntity referee = refereeId != null
                ? userRepository.findById(refereeId).orElse(null) : null;

        MatchEntity matchEntity = MatchEntity.builder()
                .tournament(tournament)
                .team1(team1)
                .team2(team2)
                .venue(venue)
                .referee(referee)
                .phase(match.getPhase())
                .scheduledAt(match.getScheduledAt())
                .status("PROGRAMADO")
                .build();

        MatchEntity saved = matchRepository.save(matchEntity);
        log.info("Scheduled match: {} vs {}", team1.getName(), team2.getName());
        return matchPersistenceMapper.toModel(saved);
    }

    @Transactional
    public MatchSummaryResponseDTO registerResult(Long organizerId, MatchResultRequestDTO dto) {
        UserEntity organizer = userRepository.findById(organizerId)
                .orElseThrow(() -> new UserNotFoundException(organizerId));
        if (organizer.getUserType() != Role.ORGANIZER)
            throw new TournamentValidationException("Only the organizer can record results.");

        MatchEntity match = matchRepository.findById(dto.getMatchId())
                .orElseThrow(() -> new UserNotFoundException(dto.getMatchId()));

        if (matchResultRepository.findByMatch_Match_id(dto.getMatchId()).isPresent())
            throw new MatchResultAlreadyExistsException(dto.getMatchId());

        MatchResultEntity result = MatchResultEntity.builder()
                .match(match)
                .registeredBy(organizer)
                .team1Goals(dto.getTeam1Goals())
                .team2Goals(dto.getTeam2Goals())
                .registeredAt(LocalDateTime.now())
                .build();

        match.setStatus("JUGADO");
        matchRepository.save(match);

        MatchResultEntity saved = matchResultRepository.save(result);
        updateStandings(match, dto.getTeam1Goals(), dto.getTeam2Goals());

        log.info("Registered match result {}: {} - {}", dto.getMatchId(), dto.getTeam1Goals(), dto.getTeam2Goals());

        return buildSummary(match, saved);
    }

    public MatchSummaryResponseDTO getMatchSummary(Long matchId) {
        MatchEntity match = matchRepository.findById(matchId)
                .orElseThrow(() -> new UserNotFoundException(matchId));

        MatchResultEntity result = matchResultRepository.findByMatch_Match_id(matchId)
                .orElseThrow(() -> new UserNotFoundException(matchId));

        return buildSummary(match, result);
    }

    private MatchSummaryResponseDTO buildSummary(MatchEntity match, MatchResultEntity result) {
        List<MatchEventResponseDTO> events = matchEventRepository
                .findAllByMatch_Match_id(match.getMatch_id())
                .stream()
                .map(e -> matchEventMapper.toDto(matchEventPersistenceMapper.toModel(e)))
                .collect(Collectors.toList());

        return MatchSummaryResponseDTO.builder()
                .matchId(match.getMatch_id())
                .team1Name(match.getTeam1().getName())
                .team1Goals(result.getTeam1Goals())
                .team2Name(match.getTeam2().getName())
                .team2Goals(result.getTeam2Goals())
                .registeredAt(result.getRegisteredAt())
                .events(events)
                .build();
    }

    @Transactional
    public MatchEvent registerEvent(MatchEvent event) {
        MatchEntity match = matchRepository.findById(event.getMatch().getMatchId())
                .orElseThrow(() -> new UserNotFoundException(event.getMatch().getMatchId()));
        UserEntity user = userRepository.findById(event.getUser().getUserId())
                .orElseThrow(() -> new UserNotFoundException(event.getUser().getUserId()));
        TeamEntity team = teamRepository.findById(event.getTeam().getId())
                .orElseThrow(() -> new UserNotFoundException(event.getTeam().getId()));

        MatchEventEntity eventEntity = MatchEventEntity.builder()
                .match(match)
                .user(user)
                .team(team)
                .eventType(event.getEventType())
                .minute(event.getMinute())
                .build();

        return matchEventPersistenceMapper.toModel(matchEventRepository.save(eventEntity));
    }

    public List<Match> getMatchesByReferee(Long refereeId) {
        return matchRepository.findAllByReferee_User_id(refereeId)
                .stream()
                .map(matchPersistenceMapper::toModel)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<Match> generateBracket(Long organizerId, Long tournamentId) {
        TournamentEntity tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new TournamentNotFoundException(tournamentId));

        List<TeamEntity> teams = new ArrayList<>(teamRepository.findAll().stream()
                .filter(t -> t.isActive()).collect(Collectors.toList()));

        if (teams.size() < 2)
            throw new TournamentValidationException("At least 2 teams are needed to generate the keys.");

        Collections.shuffle(teams);
        MatchPhase phase = resolvePhase(teams.size());
        List<MatchEntity> bracket = new ArrayList<>();

        for (int i = 0; i + 1 < teams.size(); i += 2) {
            MatchEntity matchEntity = MatchEntity.builder()
                    .tournament(tournament)
                    .team1(teams.get(i))
                    .team2(teams.get(i + 1))
                    .phase(phase)
                    .status("PROGRAMADO")
                    .build();
            bracket.add(matchEntity);
        }

        return matchRepository.saveAll(bracket)
                .stream()
                .map(matchPersistenceMapper::toModel)
                .collect(Collectors.toList());
    }

    public List<Standing> getStandings(Long tournamentId) {
        return standingRepository.findAllByTournament_Tournament_idOrderByPointsDesc(tournamentId)
                .stream()
                .map(standingPersistenceMapper::toModel)
                .collect(Collectors.toList());
    }

    public List<MatchEventResponseDTO> getTopScorers(Long tournamentId) {
        return matchEventRepository
                .findAllByEventTypeAndMatch_Tournament_Tournament_id(Event.GOL, tournamentId)
                .stream()
                .map(e -> matchEventMapper.toDto(matchEventPersistenceMapper.toModel(e)))
                .collect(Collectors.toList());
    }

    public List<Match> getMatchHistory(Long tournamentId) {
        return matchRepository.findAllByTournament_Tournament_id(tournamentId).stream()
                .filter(m -> "JUGADO".equals(m.getStatus()))
                .map(matchPersistenceMapper::toModel)
                .collect(Collectors.toList());
    }

    public List<Match> getMatchesByTournament(Long tournamentId) {
        return matchRepository.findAllByTournament_Tournament_id(tournamentId)
                .stream()
                .map(matchPersistenceMapper::toModel)
                .collect(Collectors.toList());
    }

    private void updateStandings(MatchEntity match, int team1Goals, int team2Goals) {
        StandingEntity s1 = standingRepository
                .findByTournament_Tournament_idAndTeam_Id(match.getTournament().getTournament_id(), match.getTeam1().getId())
                .orElse(StandingEntity.builder().tournament(match.getTournament()).team(match.getTeam1())
                        .played(0).won(0).drawn(0).lost(0).goalsFor(0).goalsAgainst(0).goalDifference(0).points(0).build());

        StandingEntity s2 = standingRepository
                .findByTournament_Tournament_idAndTeam_Id(match.getTournament().getTournament_id(), match.getTeam2().getId())
                .orElse(StandingEntity.builder().tournament(match.getTournament()).team(match.getTeam2())
                        .played(0).won(0).drawn(0).lost(0).goalsFor(0).goalsAgainst(0).goalDifference(0).points(0).build());

        s1.setPlayed(s1.getPlayed() + 1);
        s2.setPlayed(s2.getPlayed() + 1);
        s1.setGoalsFor(s1.getGoalsFor() + team1Goals);
        s1.setGoalsAgainst(s1.getGoalsAgainst() + team2Goals);
        s2.setGoalsFor(s2.getGoalsFor() + team2Goals);
        s2.setGoalsAgainst(s2.getGoalsAgainst() + team1Goals);

        s1.setGoalDifference(s1.getGoalsFor() - s1.getGoalsAgainst());
        s2.setGoalDifference(s2.getGoalsFor() - s2.getGoalsAgainst());

        if (team1Goals > team2Goals) {
            s1.setWon(s1.getWon() + 1);       s1.setPoints(s1.getPoints() + 3);
            s2.setLost(s2.getLost() + 1);
        } else if (team2Goals > team1Goals) {
            s2.setWon(s2.getWon() + 1);       s2.setPoints(s2.getPoints() + 3);
            s1.setLost(s1.getLost() + 1);
        } else {
            s1.setDrawn(s1.getDrawn() + 1);   s1.setPoints(s1.getPoints() + 1);
            s2.setDrawn(s2.getDrawn() + 1);   s2.setPoints(s2.getPoints() + 1);
        }

        standingRepository.save(s1);
        standingRepository.save(s2);
    }

    private MatchPhase resolvePhase(int teamCount) {
        if (teamCount <= 2)  return MatchPhase.FINAL;
        if (teamCount <= 4)  return MatchPhase.SEMIFINALS;
        if (teamCount <= 8)  return MatchPhase.QUARTERFINALS;
        return MatchPhase.GROUPSTAGE;
    }
}

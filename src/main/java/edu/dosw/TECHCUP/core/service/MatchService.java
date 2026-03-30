package edu.dosw.TECHCUP.core.service;

import edu.dosw.TECHCUP.controller.dto.request.LineUpRequestDTO;
import edu.dosw.TECHCUP.controller.dto.request.MatchEventRequestDTO;
import edu.dosw.TECHCUP.controller.dto.request.MatchRequestDTO;
import edu.dosw.TECHCUP.controller.dto.request.MatchResultRequestDTO;
import edu.dosw.TECHCUP.controller.dto.response.*;
import edu.dosw.TECHCUP.controller.mapper.*;
import edu.dosw.TECHCUP.core.exception.TournamentNotFoundException;
import edu.dosw.TECHCUP.core.exception.TournamentValidationException;
import edu.dosw.TECHCUP.core.exception.UserNotFoundException;
import edu.dosw.TECHCUP.core.model.*;
import edu.dosw.TECHCUP.core.model.enums.Event;
import edu.dosw.TECHCUP.core.model.enums.MatchPhase;
import edu.dosw.TECHCUP.core.model.enums.Role;
import edu.dosw.TECHCUP.core.util.IdGeneratorUtil;
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
    private final MatchMapper matchMapper;
    private final MatchResultMapper matchResultMapper;
    private final MatchEventMapper matchEventMapper;
    private final LineUpMapper lineUpMapper;
    private final StandingMapper standingMapper;

    @Transactional
    public MatchResponseDTO scheduleMatch(MatchRequestDTO dto) {
        Tournament tournament = tournamentRepository.findById(dto.getTournamentId())
                .orElseThrow(() -> new TournamentNotFoundException(dto.getTournamentId()));
        Team team1 = teamRepository.findById(dto.getTeam1Id())
                .orElseThrow(() -> new UserNotFoundException(String.valueOf(dto.getTeam1Id())));
        Team team2 = teamRepository.findById(dto.getTeam2Id())
                .orElseThrow(() -> new UserNotFoundException(String.valueOf(dto.getTeam2Id())));
        Venue venue = venueRepository.findById(dto.getVenueId())
                .orElseThrow(() -> new UserNotFoundException("Cancha no encontrada: " + dto.getVenueId()));

        if (team1.getId().equals(team2.getId()))
            throw new TournamentValidationException("El equipo local y visitante no pueden ser el mismo.");

        User referee = dto.getRefereeId() != null
                ? userRepository.findById(dto.getRefereeId()).orElse(null) : null;

        Match match = Match.builder()
                .tournament(tournament)
                .team1(team1)
                .team2(team2)
                .vanue(venue)
                .referee(referee)
                .phase(dto.getPhase())
                .scheduledAt(dto.getScheduledAt())
                .status("PROGRAMADO")
                .build();

        Match saved = matchRepository.save(match);
        log.info("Partido programado: {} vs {}", team1.getName(), team2.getName());
        return matchMapper.toDto(saved);
    }

    @Transactional
    public MatchResultResponseDTO registerResult(Long organizerId, MatchResultRequestDTO dto) {
        User organizer = userRepository.findById(organizerId)
                .orElseThrow(() -> new UserNotFoundException(String.valueOf(organizerId)));
        if (organizer.getUserType() != Role.ORGANIZER)
            throw new TournamentValidationException("Solo el organizador puede registrar resultados.");

        Match match = matchRepository.findById(dto.getMatchId())
                .orElseThrow(() -> new UserNotFoundException("Partido no encontrado: " + dto.getMatchId()));

        MatchResult result = MatchResult.builder()
                .match(match)
                .registeredBy(organizer)
                .team1Goals(dto.getTeam1Goals())
                .team2Goals(dto.getTeam2Goals())
                .registeredAt(LocalDateTime.now())
                .build();

        match.setStatus("JUGADO");
        matchRepository.save(match);

        MatchResult saved = matchResultRepository.save(result);

        updateStandings(match, dto.getTeam1Goals(), dto.getTeam2Goals());

        log.info("Resultado registrado partido {}: {} - {}", dto.getMatchId(), dto.getTeam1Goals(), dto.getTeam2Goals());
        return matchResultMapper.toDto(saved);
    }

    @Transactional
    public MatchEventResponseDTO registerEvent(MatchEventRequestDTO dto) {
        Match match = matchRepository.findById(dto.getMatchId())
                .orElseThrow(() -> new UserNotFoundException("Partido no encontrado: " + dto.getMatchId()));
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new UserNotFoundException(String.valueOf(dto.getUserId())));
        Team team = teamRepository.findById(dto.getTeamId())
                .orElseThrow(() -> new UserNotFoundException(String.valueOf(dto.getTeamId())));

        MatchEvent event = MatchEvent.builder()
                .match(match)
                .user(user)
                .team(team)
                .eventType(dto.getEventType())
                .minute(dto.getMinute())
                .build();

        return matchEventMapper.toDto(matchEventRepository.save(event));
    }

    @Transactional
    public LineUpResponseDTO saveLineUp(Long captainId, LineUpRequestDTO dto) {
        User captain = userRepository.findById(captainId)
                .orElseThrow(() -> new UserNotFoundException(String.valueOf(captainId)));
        if (captain.getUserType() != Role.CAPTAIN)
            throw new TournamentValidationException("Solo el capitán puede definir la alineación.");

        Match match = matchRepository.findById(dto.getMatchId())
                .orElseThrow(() -> new UserNotFoundException("Partido no encontrado: " + dto.getMatchId()));
        Team team = teamRepository.findById(dto.getTeamId())
                .orElseThrow(() -> new UserNotFoundException(String.valueOf(dto.getTeamId())));
        User player = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new UserNotFoundException(String.valueOf(dto.getUserId())));

        LineUp lineUp = LineUp.builder()
                .match(match)
                .team(team)
                .user(player)
                .role(dto.getRole())
                .position(dto.getPosition())
                .jerseyNumber(dto.getJerseyNumber())
                .build();

        return lineUpMapper.toDto(lineUpRepository.save(lineUp));
    }
    public List<LineUpResponseDTO> getLineUpByMatchAndTeam(Long matchId, Long teamId) {
        return lineUpRepository.findAllByMatch_Match_idAndTeam_Id(matchId, teamId)
                .stream().map(lineUpMapper::toDto).collect(Collectors.toList());
    }

    public List<MatchResponseDTO> getMatchesByReferee(Long refereeId) {
        return matchRepository.findAllByReferee_User_id(refereeId)
                .stream().map(matchMapper::toDto).collect(Collectors.toList());
    }

    @Transactional
    public List<MatchResponseDTO> generateBracket(Long organizerId, Long tournamentId) {
        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new TournamentNotFoundException(tournamentId));

        List<Team> teams = new ArrayList<>(teamRepository.findAll().stream()
                .filter(t -> t.isActive()).collect(Collectors.toList()));

        if (teams.size() < 2)
            throw new TournamentValidationException("Se necesitan al menos 2 equipos para generar las llaves.");

        Collections.shuffle(teams);
        MatchPhase phase = resolvePhase(teams.size());
        List<Match> bracket = new ArrayList<>();

        for (int i = 0; i + 1 < teams.size(); i += 2) {
            Match match = Match.builder()
                    .tournament(tournament)
                    .team1(teams.get(i))
                    .team2(teams.get(i + 1))
                    .phase(phase)
                    .status("PROGRAMADO")
                    .build();
            bracket.add(match);
        }

        return matchRepository.saveAll(bracket)
                .stream().map(matchMapper::toDto).collect(Collectors.toList());
    }

    public List<StandingResponseDTO> getStandings(Long tournamentId) {
        return standingRepository.findAllByTournament_Tournament_idOrderByPointsDesc(tournamentId)
                .stream().map(standingMapper::toDto).collect(Collectors.toList());
    }

    public List<MatchEventResponseDTO> getTopScorers(Long tournamentId) {
        return matchEventRepository
                .findAllByEventTypeAndMatch_Tournament_Tournament_id(Event.GOL, tournamentId)
                .stream().map(matchEventMapper::toDto).collect(Collectors.toList());
    }

    public List<MatchResponseDTO> getMatchHistory(Long tournamentId) {
        return matchRepository.findAllByTournament_Tournament_id(tournamentId).stream()
                .filter(m -> "JUGADO".equals(m.getStatus()))
                .map(matchMapper::toDto).collect(Collectors.toList());
    }

    public List<MatchResponseDTO> getMatchesByTournament(Long tournamentId) {
        return matchRepository.findAllByTournament_Tournament_id(tournamentId)
                .stream().map(matchMapper::toDto).collect(Collectors.toList());
    }

    private void updateStandings(Match match, int team1Goals, int team2Goals) {
        Standing s1 = standingRepository
                .findByTournament_Tournament_idAndTeam_Id(match.getTournament().getTournament_id(), match.getTeam1().getId())
                .orElse(Standing.builder().tournament(match.getTournament()).team(match.getTeam1())
                        .played(0).won(0).drawn(0).lost(0).goalsFor(0).goalsAgainst(0).points(0).build());

        Standing s2 = standingRepository
                .findByTournament_Tournament_idAndTeam_Id(match.getTournament().getTournament_id(), match.getTeam2().getId())
                .orElse(Standing.builder().tournament(match.getTournament()).team(match.getTeam2())
                        .played(0).won(0).drawn(0).lost(0).goalsFor(0).goalsAgainst(0).points(0).build());

        s1.setPlayed(s1.getPlayed() + 1);
        s2.setPlayed(s2.getPlayed() + 1);
        s1.setGoalsFor(s1.getGoalsFor() + team1Goals);
        s1.setGoalsAgainst(s1.getGoalsAgainst() + team2Goals);
        s2.setGoalsFor(s2.getGoalsFor() + team2Goals);
        s2.setGoalsAgainst(s2.getGoalsAgainst() + team1Goals);

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

package edu.dosw.TECHCUP.core.service;

import edu.dosw.TECHCUP.controller.dto.response.*;
import edu.dosw.TECHCUP.controller.mapper.MatchMapper;
import edu.dosw.TECHCUP.controller.mapper.StandingMapper;
import edu.dosw.TECHCUP.core.exception.TournamentNotFoundException;
import edu.dosw.TECHCUP.persistence.mapper.MatchPersistenceMapper;
import edu.dosw.TECHCUP.persistence.mapper.StandingPersistenceMapper;
import edu.dosw.TECHCUP.persistence.repository.MatchEventRepository;
import edu.dosw.TECHCUP.persistence.repository.MatchRepository;
import edu.dosw.TECHCUP.persistence.repository.StandingRepository;
import edu.dosw.TECHCUP.persistence.repository.TournamentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class StatisticsService {

    private final TournamentRepository tournamentRepository;
    private final StandingRepository standingRepository;
    private final MatchRepository matchRepository;
    private final MatchEventRepository matchEventRepository;
    private final StandingMapper standingMapper;
    private final MatchMapper matchMapper;
    private final StandingPersistenceMapper standingPersistenceMapper;
    private final MatchPersistenceMapper matchPersistenceMapper;

    public TournamentStatisticsResponseDTO getTournamentStatistics(Long tournamentId) {
        tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new TournamentNotFoundException(tournamentId));

        List<StandingResponseDTO> standings = standingRepository
                .findAllByTournament_Tournament_idOrderByPointsDesc(tournamentId)
                .stream()
                .map(e -> standingMapper.toDto(standingPersistenceMapper.toModel(e)))
                .collect(Collectors.toList());

        List<TopScorerResponseDTO> topScorers = getTopScorers(tournamentId);

        List<MatchResponseDTO> matchHistory = matchRepository
                .findAllByTournament_Tournament_id(tournamentId)
                .stream()
                .filter(m -> "JUGADO".equals(m.getStatus()))
                .map(e -> matchMapper.toDto(matchPersistenceMapper.toModel(e)))
                .collect(Collectors.toList());

        log.info("Statistics consolidated for tournament {}", tournamentId);
        return TournamentStatisticsResponseDTO.builder()
                .standings(standings)
                .topScorers(topScorers)
                .matchHistory(matchHistory)
                .build();
    }

    public List<TopScorerResponseDTO> getTopScorers(Long tournamentId) {
        tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new TournamentNotFoundException(tournamentId));

        return matchEventRepository.findTopScorersByTournament(tournamentId)
                .stream()
                .map(row -> TopScorerResponseDTO.builder()
                        .userId((Long) row[0])
                        .playerName(row[1] + " " + row[2])
                        .teamId((Long) row[3])
                        .teamName((String) row[4])
                        .totalGoals(((Long) row[5]).intValue())
                        .build())
                .collect(Collectors.toList());
    }
}
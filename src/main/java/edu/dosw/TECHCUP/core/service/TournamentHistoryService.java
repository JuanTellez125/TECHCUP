package edu.dosw.TECHCUP.core.service;

import edu.dosw.TECHCUP.controller.dto.response.*;
import edu.dosw.TECHCUP.controller.mapper.StandingMapper;
import edu.dosw.TECHCUP.controller.mapper.TeamMapper;
import edu.dosw.TECHCUP.core.exception.TournamentNotFoundException;
import edu.dosw.TECHCUP.core.exception.TournamentNotFinalizedException;
import edu.dosw.TECHCUP.core.model.enums.MatchPhase;
import edu.dosw.TECHCUP.core.model.enums.RegisterTournamentStatus;
import edu.dosw.TECHCUP.core.model.enums.TournamentStatus;
import edu.dosw.TECHCUP.persistence.entity.MatchEntity;
import edu.dosw.TECHCUP.persistence.entity.TeamEntity;
import edu.dosw.TECHCUP.persistence.entity.TournamentEntity;
import edu.dosw.TECHCUP.persistence.mapper.StandingPersistenceMapper;
import edu.dosw.TECHCUP.persistence.mapper.TeamPersistenceMapper;
import edu.dosw.TECHCUP.persistence.mapper.TournamentPersistenceMapper;
import edu.dosw.TECHCUP.persistence.repository.*;
import edu.dosw.TECHCUP.controller.mapper.TournamentMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@Slf4j
@RequiredArgsConstructor
public class TournamentHistoryService {

    private final TournamentRepository tournamentRepository;
    private final TournamentRegistrationRepository registrationRepository;
    private final StandingRepository standingRepository;
    private final MatchRepository matchRepository;
    private final StatisticsService statisticsService;

    private final TournamentMapper tournamentMapper;
    private final TournamentPersistenceMapper tournamentPersistenceMapper;
    private final StandingMapper standingMapper;
    private final StandingPersistenceMapper standingPersistenceMapper;
    private final TeamMapper teamMapper;
    private final TeamPersistenceMapper teamPersistenceMapper;


    public List<TournamentResponseDTO> getFinishedTournaments() {
        log.debug("All completed tournaments");
        return tournamentRepository.findAllByStatus(TournamentStatus.FINALIZED)
                .stream()
                .map(e -> tournamentMapper.toDto(tournamentPersistenceMapper.toModel(e)))
                .collect(Collectors.toList());
    }

    public TournamentHistoryResponseDTO getTournamentHistory(Long tournamentId) {
        log.debug("History for the tournament {}", tournamentId);

        TournamentEntity tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new TournamentNotFoundException(tournamentId));

        if (tournament.getStatus() != TournamentStatus.FINALIZED) {
            log.warn("Tournament {} has not finished (state: {})", tournamentId, tournament.getStatus());
            throw new TournamentNotFinalizedException(tournamentId);
        }

        String champion = resolveChampion(tournamentId);

        List<TeamResponseDTO> participants = resolveParticipants(tournamentId);

        List<StandingResponseDTO> finalStandings = standingRepository
                .findAllByTournament_Tournament_idOrderByPointsDesc(tournamentId)
                .stream()
                .map(e -> standingMapper.toDto(standingPersistenceMapper.toModel(e)))
                .collect(Collectors.toList());

        List<TopScorerResponseDTO> topScorers = statisticsService.getTopScorers(tournamentId);

        log.info("History for the tournament {}, champion: {}", tournamentId, champion);

        return TournamentHistoryResponseDTO.builder()
                .tournamentId(tournamentId)
                .startDate(tournament.getStartDate())
                .endDate(tournament.getEndDate())
                .champion(champion)
                .participants(participants)
                .finalStandings(finalStandings)
                .topScorers(topScorers)
                .build();
    }

    private String resolveChampion(Long tournamentId) {
        List<MatchEntity> finalMatches = matchRepository
                .findAllByTournament_Tournament_idAndPhase(tournamentId, MatchPhase.FINAL);

        if (finalMatches.isEmpty()) {
            log.warn("No matches from the FINAL phase were found for the tournament {}", tournamentId);
            return "Unknown";
        }

        MatchEntity finalMatch = finalMatches.get(0);

        if (finalMatch.getResult() == null) {
            log.warn("The FINAL match of the tournament {} has no recorded result", tournamentId);
            return "Unknown";
        }

        int goals1 = finalMatch.getResult().getTeam1Goals();
        int goals2 = finalMatch.getResult().getTeam2Goals();

        if (goals1 > goals2) {
            return finalMatch.getTeam1().getName();
        } else if (goals2 > goals1) {
            return finalMatch.getTeam2().getName();
        } else {
            return finalMatch.getTeam1().getName() + " / " + finalMatch.getTeam2().getName();
        }
    }

    private List<TeamResponseDTO> resolveParticipants(Long tournamentId) {
        return registrationRepository
                .findAllByTournament_Tournament_idAndStatus(tournamentId, RegisterTournamentStatus.APROBADO)
                .stream()
                .map(r -> teamMapper.toDto(teamPersistenceMapper.toModel(r.getTeam())))
                .collect(Collectors.toList());
    }
}
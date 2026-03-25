package edu.dosw.TECHCUP.core.service;

import edu.dosw.TECHCUP.controller.dto.request.MatchRequestDTO;
import edu.dosw.TECHCUP.controller.dto.response.MatchResponseDTO;
import edu.dosw.TECHCUP.controller.mapper.MatchMapper;
import edu.dosw.TECHCUP.core.exception.TournamentNotFoundException;
import edu.dosw.TECHCUP.core.exception.TournamentValidationException;
import edu.dosw.TECHCUP.core.exception.UserNotFoundException;
import edu.dosw.TECHCUP.core.model.Match;
import edu.dosw.TECHCUP.core.model.Team;
import edu.dosw.TECHCUP.core.model.Tournament;
import edu.dosw.TECHCUP.core.model.enums.MatchPhase;
import edu.dosw.TECHCUP.core.repository.MatchRepository;
import edu.dosw.TECHCUP.core.repository.TeamRepository;
import edu.dosw.TECHCUP.core.repository.TournamentRepository;
import edu.dosw.TECHCUP.core.util.IdGeneratorUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class MatchService {

    private final MatchRepository matchRepository;
    private final MatchMapper matchMapper;
    private final TeamRepository teamRepository;
    private final TournamentRepository tournamentRepository;

    // ── Programar un partido (sección 6.6) ──────────────────────────────────
    @Transactional
    public MatchResponseDTO scheduleMatch(MatchRequestDTO dto) {
        Tournament tournament = tournamentRepository.findById(dto.getTournamentId())
                .orElseThrow(() -> new TournamentNotFoundException(
                        Long.parseLong(dto.getTournamentId())));

        Team localTeam = teamRepository.findById(dto.getLocalTeamId())
                .orElseThrow(() -> new UserNotFoundException(dto.getLocalTeamId()));

        Team awayTeam = teamRepository.findById(dto.getAwayTeamId())
                .orElseThrow(() -> new UserNotFoundException(dto.getAwayTeamId()));

        if (localTeam.getId().equals(awayTeam.getId())) {
            throw new TournamentValidationException(
                    "El equipo local y visitante no pueden ser el mismo.");
        }

        Match match = Match.builder()
                .id(IdGeneratorUtil.generateId())
                .refereeId(dto.getRefereeId())
                .localTeam(localTeam)
                .awayTeam(awayTeam)
                .numberField(dto.getNumberField())
                .matchDateTime(dto.getMatchDateTime())
                .matchPhase(dto.getMatchPhase())
                .tournament(tournament)
                .isPlayed(false)
                .localScore(0)
                .awayScore(0)
                .build();

        Match saved = matchRepository.save(match);
        log.info("Partido programado: {} vs {}", localTeam.getTeamName(), awayTeam.getTeamName());
        return matchMapper.toDto(saved);
    }

    // ── Registrar resultado (sección 6.8) ────────────────────────────────────
    @Transactional
    public MatchResponseDTO registerResult(String matchId, int localScore, int awayScore,
                                           List<String> scorerIds,
                                           List<String> yellowCardIds,
                                           List<String> redCardIds) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new UserNotFoundException(matchId));

        match.setLocalScore(localScore);
        match.setAwayScore(awayScore);
        match.setScorerIds(scorerIds != null ? scorerIds : new ArrayList<>());
        match.setYellowCardPlayerIds(yellowCardIds != null ? yellowCardIds : new ArrayList<>());
        match.setRedCardPlayerIds(redCardIds != null ? redCardIds : new ArrayList<>());
        match.setPlayed(true);

        Match saved = matchRepository.save(match);
        log.info("Resultado registrado para partido {}: {} - {}", matchId, localScore, awayScore);
        return matchMapper.toDto(saved);
    }

    // ── Consultar partidos de un árbitro (sección 6.9) ───────────────────────
    public List<MatchResponseDTO> getMatchesByReferee(String refereeId) {
        return matchRepository.findAllByRefereeId(refereeId)
                .stream()
                .map(matchMapper::toDto)
                .collect(Collectors.toList());
    }

    // ── Consultar todos los partidos de un torneo ────────────────────────────
    public List<MatchResponseDTO> getMatchesByTournament(String tournamentId) {
        return matchRepository.findAllByTournamentId(tournamentId)
                .stream()
                .map(matchMapper::toDto)
                .collect(Collectors.toList());
    }

    // ── Generar llaves eliminatorias aleatorias (sección 6.11) ───────────────
    @Transactional
    public List<MatchResponseDTO> generateBracket(String organizerId, String tournamentId) {
        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new TournamentNotFoundException(
                        Long.parseLong(tournamentId)));

        List<Team> teams = new ArrayList<>(teamRepository.findAllByTournamentId(tournamentId));

        if (teams.size() < 2) {
            throw new TournamentValidationException(
                    "Se necesitan al menos 2 equipos para generar las llaves.");
        }

        // Sorteo aleatorio de equipos
        Collections.shuffle(teams);

        List<Match> bracket = new ArrayList<>();
        MatchPhase phase = resolvePhase(teams.size());

        for (int i = 0; i + 1 < teams.size(); i += 2) {
            Match match = Match.builder()
                    .id(IdGeneratorUtil.generateId())
                    .localTeam(teams.get(i))
                    .awayTeam(teams.get(i + 1))
                    .matchPhase(phase)
                    .tournament(tournament)
                    .isPlayed(false)
                    .localScore(0)
                    .awayScore(0)
                    .build();
            bracket.add(match);
        }

        List<Match> saved = matchRepository.saveAll(bracket);
        log.info("Llave generada para torneo {} con {} partidos", tournamentId, saved.size());
        return saved.stream().map(matchMapper::toDto).collect(Collectors.toList());
    }

    // ── Historial de partidos jugados (sección 6.12) ─────────────────────────
    public List<MatchResponseDTO> getMatchHistory(String tournamentId) {
        return matchRepository.findAllByTournamentIdAndIsPlayed(tournamentId, true)
                .stream()
                .map(matchMapper::toDto)
                .collect(Collectors.toList());
    }

    // ── Helper: determina fase según número de equipos ────────────────────────
    private MatchPhase resolvePhase(int teamCount) {
        if (teamCount <= 2)  return MatchPhase.FINAL;
        if (teamCount <= 4)  return MatchPhase.SEMIFINALS;
        if (teamCount <= 8)  return MatchPhase.QUARTERFINALS;
        return MatchPhase.GROUPSTAGE;
    }
}

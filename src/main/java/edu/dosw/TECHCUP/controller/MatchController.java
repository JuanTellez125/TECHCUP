package edu.dosw.TECHCUP.controller;

import edu.dosw.TECHCUP.controller.dto.request.LineUpRequestDTO;
import edu.dosw.TECHCUP.controller.dto.request.MatchEventRequestDTO;
import edu.dosw.TECHCUP.controller.dto.request.MatchRequestDTO;
import edu.dosw.TECHCUP.controller.dto.request.MatchResultRequestDTO;
import edu.dosw.TECHCUP.controller.dto.response.*;
import edu.dosw.TECHCUP.controller.mapper.LineUpMapper;
import edu.dosw.TECHCUP.controller.mapper.MatchEventMapper;
import edu.dosw.TECHCUP.controller.mapper.MatchMapper;
import edu.dosw.TECHCUP.controller.mapper.StandingMapper;
import edu.dosw.TECHCUP.core.model.Match;
import edu.dosw.TECHCUP.core.model.MatchEvent;
import edu.dosw.TECHCUP.core.service.LineUpService;
import edu.dosw.TECHCUP.core.service.MatchService;
import edu.dosw.TECHCUP.core.service.StatisticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/matches")
@RequiredArgsConstructor
@Tag(name = "Match", description = "Get team line-up by team and match")
public class MatchController {

    private final MatchService matchService;
    private final LineUpService lineUpService;
    private final StatisticsService statisticsService;
    private final MatchMapper matchMapper;
    private final MatchEventMapper matchEventMapper;
    private final StandingMapper standingMapper;
    private final LineUpMapper lineUpMapper;

    @PostMapping
    @PreAuthorize("hasAnyRole('ORGANIZER', 'ADMINISTRATOR')")
    @Operation(summary = "Schedule match")
    public ResponseEntity<MatchResponseDTO> scheduleMatch(@RequestBody MatchRequestDTO dto) {
        Match match = matchMapper.toModel(dto);
        Match result = matchService.scheduleMatch(match);
        return ResponseEntity.status(HttpStatus.CREATED).body(matchMapper.toDto(result));
    }

    @PostMapping("/result")
    @PreAuthorize("hasAnyRole('ORGANIZER', 'ADMINISTRATOR')")
    @Operation(summary = "Record match result (organizer only)")
    public ResponseEntity<MatchSummaryResponseDTO> registerResult(
            @RequestParam Long organizerId,
            @RequestBody MatchResultRequestDTO dto) {
        return ResponseEntity.ok(matchService.registerResult(organizerId, dto));
    }

    @GetMapping("/{matchId}/summary")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'ORGANIZER', 'REFEREE', 'CAPTAIN', 'PLAYER')")
    @Operation(summary = "Get match summary with result and events")
    public ResponseEntity<MatchSummaryResponseDTO> getMatchSummary(@PathVariable Long matchId) {
        return ResponseEntity.ok(matchService.getMatchSummary(matchId));
    }

    @PostMapping("/event")
    @PreAuthorize("hasAnyRole('REFEREE', 'ORGANIZER', 'ADMINISTRATOR')")
    @Operation(summary = "Record match event (goal, card, etc.)")
    public ResponseEntity<MatchEventResponseDTO> registerEvent(@RequestBody MatchEventRequestDTO dto) {
        MatchEvent event = matchEventMapper.toModel(dto);
        MatchEvent result = matchService.registerEvent(event);
        return ResponseEntity.status(HttpStatus.CREATED).body(matchEventMapper.toDto(result));
    }

    @PostMapping("/{matchId}/lineup")
    @PreAuthorize("hasAnyRole('CAPTAIN', 'ADMINISTRATOR')")
    @Operation(summary = "Save full team lineup (captain only)")
    public ResponseEntity<List<LineUpResponseDTO>> saveLineUp(
            @RequestParam Long captainId,
            @PathVariable Long matchId,
            @RequestBody LineUpRequestDTO dto) {
        return ResponseEntity.ok(lineUpMapper.toDtoList(lineUpService.saveLineUp(captainId, matchId, dto)));
    }

    @GetMapping("/{matchId}/lineup")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'ORGANIZER', 'REFEREE', 'CAPTAIN', 'PLAYER')")
    @Operation(summary = "Get lineup by match and team")
    public ResponseEntity<List<LineUpResponseDTO>> getLineUp(
            @PathVariable Long matchId,
            @RequestParam Long teamId) {
        return ResponseEntity.ok(lineUpMapper.toDtoList(lineUpService.getLineUp(matchId, teamId)));
    }

    @GetMapping("/referee/{refereeId}")
    @PreAuthorize("hasAnyRole('REFEREE', 'ORGANIZER', 'ADMINISTRATOR')")
    @Operation(summary = "Get matches by referee")
    public ResponseEntity<List<MatchResponseDTO>> getMatchesByReferee(@PathVariable Long refereeId) {
        return ResponseEntity.ok(
                matchService.getMatchesByReferee(refereeId).stream()
                        .map(matchMapper::toDto)
                        .collect(Collectors.toList())
        );
    }

    @PostMapping("/bracket/{tournamentId}")
    @PreAuthorize("hasAnyRole('ORGANIZER', 'ADMINISTRATOR')")
    @Operation(summary = "Generate tournament brackets")
    public ResponseEntity<List<MatchResponseDTO>> generateBracket(
            @RequestParam Long organizerId,
            @PathVariable Long tournamentId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                matchService.generateBracket(organizerId, tournamentId).stream()
                        .map(matchMapper::toDto)
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("/tournament/{tournamentId}")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'ORGANIZER', 'REFEREE', 'CAPTAIN', 'PLAYER')")
    @Operation(summary = "Get all the matches of the tournament")
    public ResponseEntity<List<MatchResponseDTO>> getMatchesByTournament(@PathVariable Long tournamentId) {
        return ResponseEntity.ok(
                matchService.getMatchesByTournament(tournamentId).stream()
                        .map(matchMapper::toDto)
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("/tournament/{tournamentId}/history")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'ORGANIZER', 'REFEREE', 'CAPTAIN', 'PLAYER')")
    @Operation(summary = "Get history of matches played")
    public ResponseEntity<List<MatchResponseDTO>> getMatchHistory(@PathVariable Long tournamentId) {
        return ResponseEntity.ok(
                matchService.getMatchHistory(tournamentId).stream()
                        .map(matchMapper::toDto)
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("/tournament/{tournamentId}/standings")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'ORGANIZER', 'REFEREE', 'CAPTAIN', 'PLAYER')")
    @Operation(summary = "Get leaderboard")
    public ResponseEntity<List<StandingResponseDTO>> getStandings(@PathVariable Long tournamentId) {
        return ResponseEntity.ok(
                matchService.getStandings(tournamentId).stream()
                        .map(standingMapper::toDto)
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("/tournament/{tournamentId}/top-scorers")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'ORGANIZER', 'REFEREE', 'CAPTAIN', 'PLAYER')")
    @Operation(summary = "Obtain top scorers of the tournament")
    public ResponseEntity<List<TopScorerResponseDTO>> getTopScorers(@PathVariable Long tournamentId) {
        return ResponseEntity.ok(statisticsService.getTopScorers(tournamentId));
    }
}

package edu.dosw.TECHCUP.core.service;

import edu.dosw.TECHCUP.controller.dto.request.MatchResultRequestDTO;
import edu.dosw.TECHCUP.controller.dto.response.*;
import edu.dosw.TECHCUP.controller.mapper.MatchEventMapper;
import edu.dosw.TECHCUP.core.exception.TournamentNotFoundException;
import edu.dosw.TECHCUP.core.exception.TournamentValidationException;
import edu.dosw.TECHCUP.core.model.*;
import edu.dosw.TECHCUP.core.model.enums.Event;
import edu.dosw.TECHCUP.core.model.enums.MatchPhase;
import edu.dosw.TECHCUP.core.model.enums.Role;
import edu.dosw.TECHCUP.core.model.enums.TournamentStatus;
import edu.dosw.TECHCUP.persistence.entity.*;
import edu.dosw.TECHCUP.persistence.mapper.*;
import edu.dosw.TECHCUP.persistence.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MatchServiceTest {

    @Mock MatchRepository matchRepository;
    @Mock MatchResultRepository matchResultRepository;
    @Mock MatchEventRepository matchEventRepository;
    @Mock LineUpRepository lineUpRepository;
    @Mock TeamRepository teamRepository;
    @Mock TournamentRepository tournamentRepository;
    @Mock VenueRepository venueRepository;
    @Mock UserRepository userRepository;
    @Mock StandingRepository standingRepository;
    @Mock MatchEventMapper matchEventMapper;
    @Mock MatchPersistenceMapper matchPersistenceMapper;
    @Mock MatchResultPersistenceMapper matchResultPersistenceMapper;
    @Mock MatchEventPersistenceMapper matchEventPersistenceMapper;
    @Mock StandingPersistenceMapper standingPersistenceMapper;

    @InjectMocks MatchService service;

    // ─── scheduleMatch ────────────────────────────────────────────────────────

    @Test
    void scheduleMatch_success() {
        TournamentEntity tournament = buildTournamentEntity(1L);
        TeamEntity team1 = buildTeamEntity(10L, "Team A");
        TeamEntity team2 = buildTeamEntity(11L, "Team B");
        VenueEntity venue = buildVenueEntity(20L);
        MatchEntity saved = buildMatchEntity(100L, tournament, team1, team2, venue);
        Match model = new Match();
        Match matchInput = buildMatchModel(1L, 10L, 11L, 20L, null);

        when(tournamentRepository.findById(1L)).thenReturn(Optional.of(tournament));
        when(teamRepository.findById(10L)).thenReturn(Optional.of(team1));
        when(teamRepository.findById(11L)).thenReturn(Optional.of(team2));
        when(venueRepository.findById(20L)).thenReturn(Optional.of(venue));
        when(matchRepository.save(any())).thenReturn(saved);
        when(matchPersistenceMapper.toModel(saved)).thenReturn(model);

        Match result = service.scheduleMatch(matchInput);

        assertThat(result).isEqualTo(model);
    }

    @Test
    void scheduleMatch_sameTeams_throws() {
        TournamentEntity tournament = buildTournamentEntity(1L);
        TeamEntity team1 = buildTeamEntity(10L, "Team A");
        VenueEntity venue = buildVenueEntity(20L);
        Match matchInput = buildMatchModel(1L, 10L, 10L, 20L, null);

        when(tournamentRepository.findById(1L)).thenReturn(Optional.of(tournament));
        when(teamRepository.findById(10L)).thenReturn(Optional.of(team1)).thenReturn(Optional.of(team1));
        when(venueRepository.findById(20L)).thenReturn(Optional.of(venue));

        assertThatThrownBy(() -> service.scheduleMatch(matchInput))
                .isInstanceOf(TournamentValidationException.class)
                .hasMessageContaining("same");
    }

    @Test
    void scheduleMatch_tournamentNotFound_throws() {
        when(tournamentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.scheduleMatch(buildMatchModel(99L, 10L, 11L, 20L, null)))
                .isInstanceOf(TournamentNotFoundException.class);
    }

    @Test
    void scheduleMatch_withReferee_setsReferee() {
        TournamentEntity tournament = buildTournamentEntity(1L);
        TeamEntity team1 = buildTeamEntity(10L, "Team A");
        TeamEntity team2 = buildTeamEntity(11L, "Team B");
        VenueEntity venue = buildVenueEntity(20L);
        UserEntity referee = buildUserEntity(30L, Role.REFEREE);
        MatchEntity saved = buildMatchEntity(100L, tournament, team1, team2, venue);
        Match model = new Match();
        Match matchInput = buildMatchModel(1L, 10L, 11L, 20L, 30L);

        when(tournamentRepository.findById(1L)).thenReturn(Optional.of(tournament));
        when(teamRepository.findById(10L)).thenReturn(Optional.of(team1));
        when(teamRepository.findById(11L)).thenReturn(Optional.of(team2));
        when(venueRepository.findById(20L)).thenReturn(Optional.of(venue));
        when(userRepository.findById(30L)).thenReturn(Optional.of(referee));
        when(matchRepository.save(any())).thenReturn(saved);
        when(matchPersistenceMapper.toModel(saved)).thenReturn(model);

        Match result = service.scheduleMatch(matchInput);

        assertThat(result).isEqualTo(model);
        verify(userRepository).findById(30L);
    }

    // ─── registerResult ───────────────────────────────────────────────────────

    @Test
    void registerResult_success() {
        UserEntity organizer = buildUserEntity(1L, Role.ORGANIZER);
        TournamentEntity tournament = buildTournamentEntity(1L);
        TeamEntity team1 = buildTeamEntity(10L, "Team A");
        TeamEntity team2 = buildTeamEntity(11L, "Team B");
        VenueEntity venue = buildVenueEntity(20L);
        MatchEntity match = buildMatchEntity(100L, tournament, team1, team2, venue);
        MatchResultEntity saved = buildMatchResultEntity(200L, match, organizer);
        MatchResultRequestDTO request = MatchResultRequestDTO.builder()
                .matchId(100L).team1Goals(2).team2Goals(1).build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(organizer));
        when(matchRepository.findById(100L)).thenReturn(Optional.of(match));
        when(matchResultRepository.save(any())).thenReturn(saved);
        when(standingRepository.findByTournament_Tournament_idAndTeam_Id(anyLong(), anyLong()))
                .thenReturn(Optional.empty());

        MatchSummaryResponseDTO result = service.registerResult(1L, request);

        assertThat(result).isNotNull();
        assertThat(match.getStatus()).isEqualTo("JUGADO");
    }

    @Test
    void registerResult_notOrganizer_throws() {
        UserEntity player = buildUserEntity(1L, Role.PLAYER);
        when(userRepository.findById(1L)).thenReturn(Optional.of(player));

        assertThatThrownBy(() -> service.registerResult(1L,
                MatchResultRequestDTO.builder().matchId(100L).team1Goals(1).team2Goals(0).build()))
                .isInstanceOf(TournamentValidationException.class)
                .hasMessageContaining("organizer");
    }

    // ─── registerEvent ────────────────────────────────────────────────────────

    @Test
    void registerEvent_success() {
        TournamentEntity tournament = buildTournamentEntity(1L);
        TeamEntity team1 = buildTeamEntity(10L, "Team A");
        TeamEntity team2 = buildTeamEntity(11L, "Team B");
        VenueEntity venue = buildVenueEntity(20L);
        MatchEntity match = buildMatchEntity(100L, tournament, team1, team2, venue);
        UserEntity player = buildUserEntity(2L, Role.PLAYER);
        MatchEventEntity saved = buildMatchEventEntity(300L, match, player, team1);
        MatchEvent model = new MatchEvent();
        MatchEvent eventInput = MatchEvent.builder()
                .match(Match.builder().matchId(100L).build())
                .user(User.builder().userId(2L).build())
                .team(Team.builder().id(10L).build())
                .eventType(Event.GOL)
                .minute(30)
                .build();

        when(matchRepository.findById(100L)).thenReturn(Optional.of(match));
        when(userRepository.findById(2L)).thenReturn(Optional.of(player));
        when(teamRepository.findById(10L)).thenReturn(Optional.of(team1));
        when(matchEventRepository.save(any())).thenReturn(saved);
        when(matchEventPersistenceMapper.toModel(saved)).thenReturn(model);

        MatchEvent result = service.registerEvent(eventInput);

        assertThat(result).isEqualTo(model);
    }

    // ─── getMatchesByReferee / getMatchHistory / getMatchesByTournament ───────

    @Test
    void getMatchesByReferee_returnsMappedList() {
        TournamentEntity tournament = buildTournamentEntity(1L);
        TeamEntity team1 = buildTeamEntity(10L, "A");
        TeamEntity team2 = buildTeamEntity(11L, "B");
        VenueEntity venue = buildVenueEntity(20L);
        MatchEntity m = buildMatchEntity(100L, tournament, team1, team2, venue);
        Match model = new Match();

        when(matchRepository.findAllByReferee_User_id(1L)).thenReturn(List.of(m));
        when(matchPersistenceMapper.toModel(m)).thenReturn(model);

        List<Match> result = service.getMatchesByReferee(1L);

        assertThat(result).hasSize(1).contains(model);
    }

    @Test
    void getMatchHistory_returnsOnlyPlayedMatches() {
        TournamentEntity tournament = buildTournamentEntity(1L);
        TeamEntity team1 = buildTeamEntity(10L, "A");
        TeamEntity team2 = buildTeamEntity(11L, "B");
        VenueEntity venue = buildVenueEntity(20L);
        MatchEntity played = buildMatchEntity(100L, tournament, team1, team2, venue);
        played.setStatus("JUGADO");
        MatchEntity scheduled = buildMatchEntity(101L, tournament, team1, team2, venue);
        scheduled.setStatus("PROGRAMADO");
        Match model = new Match();

        when(matchRepository.findAllByTournament_Tournament_id(1L)).thenReturn(List.of(played, scheduled));
        when(matchPersistenceMapper.toModel(played)).thenReturn(model);

        List<Match> result = service.getMatchHistory(1L);

        assertThat(result).hasSize(1).contains(model);
    }

    @Test
    void getMatchesByTournament_returnsMappedList() {
        TournamentEntity tournament = buildTournamentEntity(1L);
        TeamEntity team1 = buildTeamEntity(10L, "A");
        TeamEntity team2 = buildTeamEntity(11L, "B");
        VenueEntity venue = buildVenueEntity(20L);
        MatchEntity m = buildMatchEntity(100L, tournament, team1, team2, venue);
        Match model = new Match();

        when(matchRepository.findAllByTournament_Tournament_id(1L)).thenReturn(List.of(m));
        when(matchPersistenceMapper.toModel(m)).thenReturn(model);

        List<Match> result = service.getMatchesByTournament(1L);

        assertThat(result).hasSize(1).contains(model);
    }

    // ─── generateBracket ──────────────────────────────────────────────────────

    @Test
    void generateBracket_withTwoTeams_createsFinalPhase() {
        TournamentEntity tournament = buildTournamentEntity(1L);
        TeamEntity team1 = buildTeamEntity(10L, "A");
        TeamEntity team2 = buildTeamEntity(11L, "B");
        MatchEntity saved = buildMatchEntity(100L, tournament, team1, team2, null);
        Match model = new Match();

        when(tournamentRepository.findById(1L)).thenReturn(Optional.of(tournament));
        when(teamRepository.findAll()).thenReturn(List.of(team1, team2));
        when(matchRepository.saveAll(anyList())).thenReturn(List.of(saved));
        when(matchPersistenceMapper.toModel(saved)).thenReturn(model);

        List<Match> result = service.generateBracket(1L, 1L);

        assertThat(result).hasSize(1);
    }

    @Test
    void generateBracket_lessThanTwoTeams_throws() {
        TournamentEntity tournament = buildTournamentEntity(1L);
        TeamEntity team1 = buildTeamEntity(10L, "A");

        when(tournamentRepository.findById(1L)).thenReturn(Optional.of(tournament));
        when(teamRepository.findAll()).thenReturn(List.of(team1));

        assertThatThrownBy(() -> service.generateBracket(1L, 1L))
                .isInstanceOf(TournamentValidationException.class)
                .hasMessageContaining("2 teams");
    }

    // ─── Helpers ──────────────────────────────────────────────────────────────

    private Match buildMatchModel(Long tournamentId, Long team1Id, Long team2Id,
                                   Long venueId, Long refereeId) {
        Match.MatchBuilder builder = Match.builder()
                .tournament(Tournament.builder().tournamentId(tournamentId).build())
                .team1(Team.builder().id(team1Id).build())
                .team2(Team.builder().id(team2Id).build())
                .venue(Venue.builder().venueId(venueId).build())
                .phase(MatchPhase.GROUPSTAGE)
                .scheduledAt(LocalDateTime.now().plusDays(5));
        if (refereeId != null) {
            builder.referee(User.builder().userId(refereeId).build());
        }
        return builder.build();
    }

    private UserEntity buildUserEntity(Long id, Role role) {
        return UserEntity.builder()
                .user_id(id)
                .userType(role)
                .active(true)
                .build();
    }

    private TournamentEntity buildTournamentEntity(Long id) {
        return TournamentEntity.builder()
                .tournament_id(id)
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(30))
                .totalTeams(8)
                .status(TournamentStatus.ACTIVE)
                .build();
    }

    private TeamEntity buildTeamEntity(Long id, String name) {
        return TeamEntity.builder()
                .id(id)
                .name(name)
                .active(true)
                .build();
    }

    private VenueEntity buildVenueEntity(Long id) {
        return VenueEntity.builder()
                .venueId(id)
                .name("Stadium")
                .build();
    }

    private MatchEntity buildMatchEntity(Long id, TournamentEntity tournament,
                                          TeamEntity team1, TeamEntity team2, VenueEntity venue) {
        return MatchEntity.builder()
                .match_id(id)
                .tournament(tournament)
                .team1(team1)
                .team2(team2)
                .venue(venue)
                .phase(MatchPhase.GROUPSTAGE)
                .status("PROGRAMADO")
                .build();
    }

    private MatchResultEntity buildMatchResultEntity(Long id, MatchEntity match, UserEntity registeredBy) {
        return MatchResultEntity.builder()
                .matchResult_id(id)
                .match(match)
                .registeredBy(registeredBy)
                .team1Goals(2)
                .team2Goals(1)
                .build();
    }

    private MatchEventEntity buildMatchEventEntity(Long id, MatchEntity match,
                                                    UserEntity user, TeamEntity team) {
        return MatchEventEntity.builder()
                .matchEvent_id(id)
                .match(match)
                .user(user)
                .team(team)
                .eventType(Event.GOL)
                .minute(30)
                .build();
    }
}

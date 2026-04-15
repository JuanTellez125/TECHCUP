package edu.dosw.TECHCUP.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import edu.dosw.TECHCUP.controller.dto.request.LineUpEntryDTO;
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
import edu.dosw.TECHCUP.core.model.LineUp;
import edu.dosw.TECHCUP.core.model.Standing;
import edu.dosw.TECHCUP.core.model.enums.Event;
import edu.dosw.TECHCUP.core.model.enums.LineUpRole;
import edu.dosw.TECHCUP.core.model.enums.MatchPhase;
import edu.dosw.TECHCUP.core.service.LineUpService;
import edu.dosw.TECHCUP.core.service.MatchService;
import edu.dosw.TECHCUP.core.service.StatisticsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class MatchControllerTest {

    MockMvc mockMvc;
    ObjectMapper objectMapper;

    @Mock MatchService matchService;
    @Mock LineUpService lineUpService;
    @Mock StatisticsService statisticsService;
    @Mock MatchMapper matchMapper;
    @Mock MatchEventMapper matchEventMapper;
    @Mock StandingMapper standingMapper;
    @Mock LineUpMapper lineUpMapper;

    @InjectMocks MatchController controller;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void scheduleMatch_returns201() throws Exception {
        MatchRequestDTO request = MatchRequestDTO.builder()
                .tournamentId(1L).team1Id(10L).team2Id(11L).venueId(20L)
                .phase(MatchPhase.GROUPSTAGE).scheduledAt(LocalDateTime.now().plusDays(5))
                .build();
        when(matchService.scheduleMatch(any())).thenReturn(new Match());
        when(matchMapper.toDto(any())).thenReturn(new MatchResponseDTO());

        mockMvc.perform(post("/api/matches")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    void registerResult_returns200() throws Exception {
        MatchResultRequestDTO request = MatchResultRequestDTO.builder()
                .matchId(100L).team1Goals(2).team2Goals(1).build();
        when(matchService.registerResult(eq(1L), any())).thenReturn(new MatchSummaryResponseDTO());

        mockMvc.perform(post("/api/matches/result")
                        .param("organizerId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void registerEvent_returns201() throws Exception {
        MatchEventRequestDTO request = MatchEventRequestDTO.builder()
                .matchId(100L).userId(2L).teamId(10L).eventType(Event.GOL).minute(30).build();
        when(matchService.registerEvent(any())).thenReturn(new MatchEvent());
        when(matchEventMapper.toDto(any())).thenReturn(new MatchEventResponseDTO());

        mockMvc.perform(post("/api/matches/event")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    void saveLineUp_returns200() throws Exception {
        LineUpEntryDTO entry = LineUpEntryDTO.builder()
                .userId(2L).role(LineUpRole.TITULAR)
                .position("Portero").jerseyNumber(1).build();
        LineUpRequestDTO request = LineUpRequestDTO.builder()
                .teamId(10L).players(List.of(entry)).build();
        when(lineUpService.saveLineUp(eq(1L), eq(100L), any())).thenReturn(List.of(new LineUp()));
        when(lineUpMapper.toDtoList(any())).thenReturn(List.of(new LineUpResponseDTO()));

        mockMvc.perform(post("/api/matches/100/lineup")
                        .param("captainId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void getLineUpByMatchAndTeam_returns200() throws Exception {
        when(lineUpService.getLineUp(100L, 10L)).thenReturn(List.of(new LineUp()));
        when(lineUpMapper.toDtoList(any())).thenReturn(List.of(new LineUpResponseDTO()));

        mockMvc.perform(get("/api/matches/100/lineup")
                        .param("teamId", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void getMatchesByReferee_returns200() throws Exception {
        when(matchService.getMatchesByReferee(1L)).thenReturn(List.of(new Match()));
        when(matchMapper.toDto(any())).thenReturn(new MatchResponseDTO());

        mockMvc.perform(get("/api/matches/referee/1"))
                .andExpect(status().isOk());
    }

    @Test
    void generateBracket_returns201() throws Exception {
        when(matchService.generateBracket(1L, 10L)).thenReturn(List.of(new Match()));
        when(matchMapper.toDto(any())).thenReturn(new MatchResponseDTO());

        mockMvc.perform(post("/api/matches/bracket/10")
                        .param("organizerId", "1"))
                .andExpect(status().isCreated());
    }

    @Test
    void getMatchesByTournament_returns200() throws Exception {
        when(matchService.getMatchesByTournament(10L)).thenReturn(List.of(new Match()));
        when(matchMapper.toDto(any())).thenReturn(new MatchResponseDTO());

        mockMvc.perform(get("/api/matches/tournament/10"))
                .andExpect(status().isOk());
    }

    @Test
    void getMatchHistory_returns200() throws Exception {
        when(matchService.getMatchHistory(10L)).thenReturn(List.of(new Match()));
        when(matchMapper.toDto(any())).thenReturn(new MatchResponseDTO());

        mockMvc.perform(get("/api/matches/tournament/10/history"))
                .andExpect(status().isOk());
    }

    @Test
    void getStandings_returns200() throws Exception {
        when(matchService.getStandings(10L)).thenReturn(List.of(new Standing()));
        when(standingMapper.toDto(any())).thenReturn(new StandingResponseDTO());

        mockMvc.perform(get("/api/matches/tournament/10/standings"))
                .andExpect(status().isOk());
    }

    @Test
    void getTopScorers_returns200() throws Exception {
        when(statisticsService.getTopScorers(10L)).thenReturn(List.of());

        mockMvc.perform(get("/api/matches/tournament/10/top-scorers"))
                .andExpect(status().isOk());
    }
}

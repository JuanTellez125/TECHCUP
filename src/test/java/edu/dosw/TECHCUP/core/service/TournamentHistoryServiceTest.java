package edu.dosw.TECHCUP.core.service;

import edu.dosw.TECHCUP.controller.GlobalExceptionHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.dosw.TECHCUP.controller.TournamentController;
import edu.dosw.TECHCUP.controller.dto.response.*;
import edu.dosw.TECHCUP.controller.mapper.*;
import edu.dosw.TECHCUP.core.exception.TournamentNotFoundException;
import edu.dosw.TECHCUP.core.exception.TournamentNotFinalizedException;
import edu.dosw.TECHCUP.core.model.Tournament;
import edu.dosw.TECHCUP.core.model.enums.MatchPhase;
import edu.dosw.TECHCUP.core.model.enums.RegisterTournamentStatus;
import edu.dosw.TECHCUP.core.model.enums.TournamentStatus;
import edu.dosw.TECHCUP.core.service.StatisticsService;
import edu.dosw.TECHCUP.core.service.TournamentHistoryService;
import edu.dosw.TECHCUP.core.service.TournamentService;
import edu.dosw.TECHCUP.persistence.entity.*;
import edu.dosw.TECHCUP.persistence.mapper.*;
import edu.dosw.TECHCUP.persistence.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(MockitoExtension.class)
class TournamentHistoryServiceTest {

    @Mock TournamentRepository tournamentRepository;
    @Mock TournamentRegistrationRepository registrationRepository;
    @Mock StandingRepository standingRepository;
    @Mock MatchRepository matchRepository;
    @Mock StatisticsService statisticsService;
    @Mock TournamentMapper tournamentMapper;
    @Mock TournamentPersistenceMapper tournamentPersistenceMapper;
    @Mock StandingMapper standingMapper;
    @Mock StandingPersistenceMapper standingPersistenceMapper;
    @Mock TeamMapper teamMapper;
    @Mock TeamPersistenceMapper teamPersistenceMapper;

    @InjectMocks TournamentHistoryService historyService;

    @Mock TournamentService tournamentService;
    @Mock TournamentHistoryService historyServiceMock;

    @InjectMocks TournamentController controller;

    MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void getFinishedTournaments_returnsOnlyFinalizedTournaments() {
        TournamentEntity entity = buildFinalizedEntity();
        Tournament model = buildFinalizedModel();
        TournamentResponseDTO dto = buildTournamentResponseDTO();

        when(tournamentRepository.findAllByStatus(TournamentStatus.FINALIZED))
                .thenReturn(List.of(entity));
        when(tournamentPersistenceMapper.toModel(entity)).thenReturn(model);
        when(tournamentMapper.toDto(model)).thenReturn(dto);

        List<TournamentResponseDTO> result = historyService.getFinishedTournaments();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getStatus()).isEqualTo(TournamentStatus.FINALIZED);
        verify(tournamentRepository).findAllByStatus(TournamentStatus.FINALIZED);
    }

    @Test
    void getFinishedTournaments_returnsEmptyListWhenNoneExist() {
        when(tournamentRepository.findAllByStatus(TournamentStatus.FINALIZED))
                .thenReturn(List.of());

        List<TournamentResponseDTO> result = historyService.getFinishedTournaments();

        assertThat(result).isEmpty();
    }


    @Test
    void getTournamentHistory_throwsNotFoundWhenTournamentDoesNotExist() {
        when(tournamentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> historyService.getTournamentHistory(99L))
                .isInstanceOf(TournamentNotFoundException.class);
    }

    @Test
    void getTournamentHistory_throwsNotFinalizedWhenStatusIsNotFinalized() {
        TournamentEntity entity = TournamentEntity.builder()
                .tournament_id(1L)
                .status(TournamentStatus.INPROGRESS)
                .startDate(LocalDate.now().minusDays(10))
                .endDate(LocalDate.now().plusDays(5))
                .build();

        when(tournamentRepository.findById(1L)).thenReturn(Optional.of(entity));

        assertThatThrownBy(() -> historyService.getTournamentHistory(1L))
                .isInstanceOf(TournamentNotFinalizedException.class)
                .hasMessageContaining("No ha finalizado");
    }

    @Test
    void getTournamentHistory_identifiesChampionCorrectly() {
        TournamentEntity tournament = buildFinalizedEntity();
        when(tournamentRepository.findById(1L)).thenReturn(Optional.of(tournament));

        TeamEntity team1 = TeamEntity.builder().id(10L).name("Team Alpha").build();
        TeamEntity team2 = TeamEntity.builder().id(11L).name("Team Beta").build();
        MatchResultEntity result = MatchResultEntity.builder()
                .team1Goals(2).team2Goals(0).build();
        MatchEntity finalMatch = MatchEntity.builder()
                .team1(team1).team2(team2).phase(MatchPhase.FINAL).result(result).build();

        when(matchRepository.findAllByTournament_Tournament_idAndPhase(1L, MatchPhase.FINAL))
                .thenReturn(List.of(finalMatch));

        when(registrationRepository.findAllByTournament_Tournament_idAndStatus(
                1L, RegisterTournamentStatus.APROBADO)).thenReturn(List.of());
        when(standingRepository.findAllByTournament_Tournament_idOrderByPointsDesc(1L))
                .thenReturn(List.of());
        when(statisticsService.getTopScorers(1L)).thenReturn(List.of());

        TournamentHistoryResponseDTO history = historyService.getTournamentHistory(1L);

        assertThat(history.getChampion()).isEqualTo("Team Alpha");
    }

    @Test
    void getTournamentHistory_returnsUnknownWhenNoFinalMatchExists() {
        TournamentEntity tournament = buildFinalizedEntity();
        when(tournamentRepository.findById(1L)).thenReturn(Optional.of(tournament));
        when(matchRepository.findAllByTournament_Tournament_idAndPhase(1L, MatchPhase.FINAL))
                .thenReturn(List.of());
        when(registrationRepository.findAllByTournament_Tournament_idAndStatus(
                1L, RegisterTournamentStatus.APROBADO)).thenReturn(List.of());
        when(standingRepository.findAllByTournament_Tournament_idOrderByPointsDesc(1L))
                .thenReturn(List.of());
        when(statisticsService.getTopScorers(1L)).thenReturn(List.of());

        TournamentHistoryResponseDTO history = historyService.getTournamentHistory(1L);

        assertThat(history.getChampion()).isEqualTo("Unknown");
    }

    @Test
    void getHistorial_returns200WithList() throws Exception {
        TournamentResponseDTO dto = buildTournamentResponseDTO();
        when(historyServiceMock.getFinishedTournaments()).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/tournaments/historial"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void getHistorial_returns200WithEmptyListWhenNoFinalizedTournaments() throws Exception {
        when(historyServiceMock.getFinishedTournaments()).thenReturn(List.of());

        mockMvc.perform(get("/api/tournaments/historial"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void getTournamentHistorial_returns200WithFullHistory() throws Exception {
        TournamentHistoryResponseDTO history = TournamentHistoryResponseDTO.builder()
                .tournamentId(1L)
                .champion("Team Alpha")
                .startDate(LocalDate.of(2024, 1, 1))
                .endDate(LocalDate.of(2024, 2, 1))
                .participants(List.of())
                .finalStandings(List.of())
                .topScorers(List.of())
                .build();

        when(historyServiceMock.getTournamentHistory(1L)).thenReturn(history);

        mockMvc.perform(get("/api/tournaments/1/historial"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.champion").value("Team Alpha"))
                .andExpect(jsonPath("$.tournamentId").value(1));
    }

    @Test
    void getTournamentHistorial_returns404WhenTournamentNotFound() throws Exception {
        when(historyServiceMock.getTournamentHistory(99L))
                .thenThrow(new TournamentNotFoundException(99L));

        mockMvc.perform(get("/api/tournaments/99/historial"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getTournamentHistorial_returns400WhenNotFinalized() throws Exception {
        when(historyServiceMock.getTournamentHistory(2L))
                .thenThrow(new TournamentNotFinalizedException(2L));

        mockMvc.perform(get("/api/tournaments/2/historial"))
                .andExpect(status().isBadRequest());
    }


    private TournamentEntity buildFinalizedEntity() {
        return TournamentEntity.builder()
                .tournament_id(1L)
                .status(TournamentStatus.FINALIZED)
                .startDate(LocalDate.of(2024, 1, 1))
                .endDate(LocalDate.of(2024, 2, 1))
                .build();
    }

    private Tournament buildFinalizedModel() {
        return Tournament.builder()
                .tournamentId(1L)
                .status(TournamentStatus.FINALIZED)
                .startDate(LocalDate.of(2024, 1, 1))
                .endDate(LocalDate.of(2024, 2, 1))
                .build();
    }

    private TournamentResponseDTO buildTournamentResponseDTO() {
        return TournamentResponseDTO.builder()
                .id(1L)
                .status(TournamentStatus.FINALIZED)
                .startDate(LocalDate.of(2024, 1, 1))
                .endDate(LocalDate.of(2024, 2, 1))
                .build();
    }
}
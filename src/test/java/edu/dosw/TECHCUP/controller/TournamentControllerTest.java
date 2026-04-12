package edu.dosw.TECHCUP.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import edu.dosw.TECHCUP.controller.dto.request.TournamentConfigRequestDTO;
import edu.dosw.TECHCUP.controller.dto.request.TournamentRequestDTO;
import edu.dosw.TECHCUP.controller.dto.request.VenueRequestDTO;
import edu.dosw.TECHCUP.controller.dto.response.TournamentConfigResponseDTO;
import edu.dosw.TECHCUP.controller.dto.response.TournamentResponseDTO;
import edu.dosw.TECHCUP.controller.dto.response.VenueResponseDTO;
import edu.dosw.TECHCUP.core.service.TournamentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class TournamentControllerTest {

    MockMvc mockMvc;
    ObjectMapper objectMapper;

    @Mock TournamentService tournamentService;

    @InjectMocks TournamentController controller;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void createTournament_returns201() throws Exception {
        TournamentRequestDTO request = TournamentRequestDTO.builder()
                .startDate(LocalDate.now().plusDays(1))
                .endDate(LocalDate.now().plusDays(30))
                .totalTeams(8)
                .registrationCost(50.0)
                .build();
        TournamentResponseDTO response = new TournamentResponseDTO();

        when(tournamentService.createTournament(eq(1L), any())).thenReturn(response);

        mockMvc.perform(post("/api/tournaments")
                        .param("organizerId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    void getAllTournaments_returns200() throws Exception {
        when(tournamentService.getAllTournaments()).thenReturn(List.of(new TournamentResponseDTO()));

        mockMvc.perform(get("/api/tournaments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void getFinalizedTournaments_returns200() throws Exception {
        when(tournamentService.getFinalizedTournaments()).thenReturn(List.of(new TournamentResponseDTO()));

        mockMvc.perform(get("/api/tournaments/finalized"))
                .andExpect(status().isOk());
    }

    @Test
    void getTournamentById_returns200() throws Exception {
        when(tournamentService.getTournamentById("10")).thenReturn(new TournamentResponseDTO());

        mockMvc.perform(get("/api/tournaments/10"))
                .andExpect(status().isOk());
    }

    @Test
    void startTournament_returns200() throws Exception {
        when(tournamentService.startTournament(1L, 10L)).thenReturn(new TournamentResponseDTO());

        mockMvc.perform(patch("/api/tournaments/10/start")
                        .param("organizerId", "1"))
                .andExpect(status().isOk());
    }

    @Test
    void finishTournament_returns200() throws Exception {
        when(tournamentService.finishTournament(1L, 10L)).thenReturn(new TournamentResponseDTO());

        mockMvc.perform(patch("/api/tournaments/10/finish")
                        .param("organizerId", "1"))
                .andExpect(status().isOk());
    }

    @Test
    void configTournament_returns200() throws Exception {
        TournamentConfigRequestDTO request = TournamentConfigRequestDTO.builder()
                .rulebook("Rules v1")
                .build();
        when(tournamentService.configTournament(eq(1L), eq(10L), any())).thenReturn(new TournamentConfigResponseDTO());

        mockMvc.perform(post("/api/tournaments/10/config")
                        .param("organizerId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void addVenue_returns201() throws Exception {
        VenueRequestDTO request = VenueRequestDTO.builder()
                .name("Stadium A")
                .venueLocation("Bogota")
                .description("Main field")
                .build();
        when(tournamentService.addVenue(eq(1L), eq(10L), any())).thenReturn(new VenueResponseDTO());

        mockMvc.perform(post("/api/tournaments/10/venues")
                        .param("organizerId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    void getVenuesByTournament_returns200() throws Exception {
        when(tournamentService.getVenuesByTournament(10L)).thenReturn(List.of(new VenueResponseDTO()));

        mockMvc.perform(get("/api/tournaments/10/venues"))
                .andExpect(status().isOk());
    }
}

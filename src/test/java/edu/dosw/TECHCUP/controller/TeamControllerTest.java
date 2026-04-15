package edu.dosw.TECHCUP.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.dosw.TECHCUP.controller.dto.request.TeamRequestDTO;
import edu.dosw.TECHCUP.controller.dto.response.InvitationResponseDTO;
import edu.dosw.TECHCUP.controller.dto.response.SportProfileResponseDTO;
import edu.dosw.TECHCUP.controller.dto.response.TeamResponseDTO;
import edu.dosw.TECHCUP.controller.mapper.InvitationMapper;
import edu.dosw.TECHCUP.controller.mapper.SportProfileMapper;
import edu.dosw.TECHCUP.controller.mapper.TeamMapper;
import edu.dosw.TECHCUP.core.model.Invitation;
import edu.dosw.TECHCUP.core.model.SportProfile;
import edu.dosw.TECHCUP.core.model.Team;
import edu.dosw.TECHCUP.core.service.TeamService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class TeamControllerTest {

    MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();

    @Mock TeamService teamService;
    @Mock TeamMapper teamMapper;
    @Mock InvitationMapper invitationMapper;
    @Mock SportProfileMapper sportProfileMapper;

    @InjectMocks TeamController controller;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void createTeam_returns201() throws Exception {
        TeamRequestDTO request = TeamRequestDTO.builder().name("Los Campeones").build();
        when(teamService.createTeam(eq(1L), any())).thenReturn(new Team());
        when(teamMapper.toDto(any())).thenReturn(new TeamResponseDTO());

        mockMvc.perform(post("/api/teams")
                        .param("captainId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    void getTeamById_returns200() throws Exception {
        when(teamService.getTeamById(10L)).thenReturn(new Team());
        when(teamMapper.toDto(any())).thenReturn(new TeamResponseDTO());

        mockMvc.perform(get("/api/teams/10"))
                .andExpect(status().isOk());
    }

    @Test
    void getTeamsByTournament_returns200() throws Exception {
        when(teamService.getTeamsByTournament(20L)).thenReturn(List.of(new Team()));
        when(teamMapper.toDto(any())).thenReturn(new TeamResponseDTO());

        mockMvc.perform(get("/api/teams/tournament/20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void invitePlayer_returns201() throws Exception {
        when(teamService.invitePlayer(1L, 10L, 2L)).thenReturn(new Invitation());
        when(invitationMapper.toDto(any())).thenReturn(new InvitationResponseDTO());

        mockMvc.perform(post("/api/teams/10/invite")
                        .param("captainId", "1")
                        .param("playerId", "2"))
                .andExpect(status().isCreated());
    }

    @Test
    void respondInvitation_returns200() throws Exception {
        when(teamService.respondInvitation(2L, 100L, true)).thenReturn(new Invitation());
        when(invitationMapper.toDto(any())).thenReturn(new InvitationResponseDTO());

        mockMvc.perform(patch("/api/teams/invitations/100/respond")
                        .param("playerId", "2")
                        .param("accept", "true"))
                .andExpect(status().isOk());
    }

    @Test
    void getInvitationsByPlayer_returns200() throws Exception {
        when(teamService.getInvitationsByPlayer(2L)).thenReturn(List.of(new Invitation()));
        when(invitationMapper.toDto(any())).thenReturn(new InvitationResponseDTO());

        mockMvc.perform(get("/api/teams/invitations/player/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void searchAvailablePlayers_returns200() throws Exception {
        when(teamService.searchAvailablePlayers(any(), any(), any(), any(), any()))
                .thenReturn(List.of(new SportProfile()));
        when(sportProfileMapper.toDto(any())).thenReturn(new SportProfileResponseDTO());

        mockMvc.perform(get("/api/teams/players/available"))
                .andExpect(status().isOk());
    }

    @Test
    void validateTeam_returns200() throws Exception {
        doNothing().when(teamService).validateTeam(10L);

        mockMvc.perform(post("/api/teams/10/validate"))
                .andExpect(status().isOk());
    }
}

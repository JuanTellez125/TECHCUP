package edu.dosw.TECHCUP.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.dosw.TECHCUP.controller.dto.request.LineUpEntryDTO;
import edu.dosw.TECHCUP.controller.dto.request.LineUpRequestDTO;
import edu.dosw.TECHCUP.controller.dto.response.LineUpResponseDTO;
import edu.dosw.TECHCUP.core.model.enums.LineUpRole;
import edu.dosw.TECHCUP.core.service.LineUpService;
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
class LineUpControllerTest {

    MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();

    @Mock LineUpService lineUpService;

    @InjectMocks LineUpController controller;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void saveLineUp_returns200() throws Exception {
        LineUpEntryDTO entry = LineUpEntryDTO.builder()
                .userId(2L).role(LineUpRole.TITULAR)
                .position("Portero").jerseyNumber(1).build();
        LineUpRequestDTO request = LineUpRequestDTO.builder()
                .teamId(10L)
                .players(List.of(entry))
                .build();
        LineUpResponseDTO response = new LineUpResponseDTO();

        when(lineUpService.saveLineUp(eq(1L), eq(100L), any())).thenReturn(List.of(response));

        mockMvc.perform(post("/api/lineups")
                        .param("captainId", "1")
                        .param("matchId", "100")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void getLineUp_returns200() throws Exception {
        LineUpResponseDTO response = new LineUpResponseDTO();
        when(lineUpService.getLineUp(100L, 10L)).thenReturn(List.of(response));

        mockMvc.perform(get("/api/lineups")
                        .param("matchId", "100")
                        .param("teamId", "10"))
                .andExpect(status().isOk());
    }
}

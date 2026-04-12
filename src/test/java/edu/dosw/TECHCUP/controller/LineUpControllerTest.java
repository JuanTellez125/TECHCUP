package edu.dosw.TECHCUP.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
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
        LineUpRequestDTO request = LineUpRequestDTO.builder()
                .matchId(100L).teamId(10L).userId(2L).role(LineUpRole.TITULAR)
                .position("Portero").jerseyNumber(1).build();
        LineUpResponseDTO response = new LineUpResponseDTO();

        when(lineUpService.saveLineUp(eq(1L), any())).thenReturn(response);

        mockMvc.perform(post("/api/lineups")
                        .param("captainId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void getLineUp_returns200() throws Exception {
        LineUpResponseDTO response = new LineUpResponseDTO();
        when(lineUpService.getLineUp(10L, 100L)).thenReturn(response);

        mockMvc.perform(get("/api/lineups")
                        .param("teamId", "10")
                        .param("matchId", "100"))
                .andExpect(status().isOk());
    }
}

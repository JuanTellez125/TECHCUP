package edu.dosw.TECHCUP.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.dosw.TECHCUP.controller.dto.request.SportProfileRequestDTO;
import edu.dosw.TECHCUP.controller.dto.request.UserRequestDTO;
import edu.dosw.TECHCUP.controller.dto.response.SportProfileResponseDTO;
import edu.dosw.TECHCUP.controller.dto.response.UserResponseDTO;
import edu.dosw.TECHCUP.controller.mapper.UserMapper;
import edu.dosw.TECHCUP.core.model.User;
import edu.dosw.TECHCUP.core.model.enums.Position;
import edu.dosw.TECHCUP.core.model.enums.Role;
import edu.dosw.TECHCUP.core.service.impl.PlayerService;
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
class PlayerControllerTest {

    MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();

    @Mock PlayerService playerService;
    @Mock UserMapper userMapper;

    @InjectMocks PlayerController controller;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void createPlayer_returns201() throws Exception {
        UserRequestDTO request = buildUserRequest();
        User model = buildUser();
        UserResponseDTO response = buildUserResponse();

        when(userMapper.toModel(any(UserRequestDTO.class))).thenReturn(model);
        when(playerService.createUser(model)).thenReturn(model);
        when(userMapper.toDto(model)).thenReturn(response);

        mockMvc.perform(post("/api/players")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    void getAllPlayers_returns200() throws Exception {
        User model = buildUser();
        UserResponseDTO response = buildUserResponse();

        when(playerService.getAllPlayers()).thenReturn(List.of(model));
        when(userMapper.toDto(model)).thenReturn(response);

        mockMvc.perform(get("/api/players"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void getPlayerById_returns200() throws Exception {
        User model = buildUser();
        UserResponseDTO response = buildUserResponse();

        when(playerService.getPlayerById(1L)).thenReturn(model);
        when(userMapper.toDto(model)).thenReturn(response);

        mockMvc.perform(get("/api/players/1"))
                .andExpect(status().isOk());
    }

    @Test
    void updatePlayer_returns200() throws Exception {
        UserRequestDTO request = buildUserRequest();
        User model = buildUser();
        UserResponseDTO response = buildUserResponse();

        when(userMapper.toModel(any(UserRequestDTO.class))).thenReturn(model);
        when(playerService.updateUser(eq(1L), any())).thenReturn(model);
        when(userMapper.toDto(model)).thenReturn(response);

        mockMvc.perform(put("/api/players/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void deletePlayer_returns204() throws Exception {
        doNothing().when(playerService).deleteUser(1L);

        mockMvc.perform(delete("/api/players/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void saveSportProfile_returns200() throws Exception {
        SportProfileRequestDTO request = SportProfileRequestDTO.builder()
                .primaryPosition(Position.PORTERO)
                .available(true)
                .build();
        SportProfileResponseDTO response = new SportProfileResponseDTO();

        when(playerService.saveSportProfile(eq(1L), any())).thenReturn(response);

        mockMvc.perform(post("/api/players/1/sport-profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void getSportProfile_returns200() throws Exception {
        SportProfileResponseDTO response = new SportProfileResponseDTO();
        when(playerService.getSportProfile(1L)).thenReturn(response);

        mockMvc.perform(get("/api/players/1/sport-profile"))
                .andExpect(status().isOk());
    }

    @Test
    void setAvailability_returns200() throws Exception {
        SportProfileResponseDTO response = new SportProfileResponseDTO();
        when(playerService.setAvailability(1L, true)).thenReturn(response);

        mockMvc.perform(patch("/api/players/1/availability")
                        .param("available", "true"))
                .andExpect(status().isOk());
    }

    // ─── Helpers ──────────────────────────────────────────────────────────────

    private UserRequestDTO buildUserRequest() {
        return UserRequestDTO.builder()
                .firstName("Luis")
                .lastName("Rodriguez")
                .email("player@gmail.com")
                .password("password123")
                .documentId("111222")
                .userType(Role.PLAYER)
                .build();
    }

    private User buildUser() {
        return User.builder()
                .userId(1L)
                .firstName("Luis")
                .lastName("Rodriguez")
                .email("player@gmail.com")
                .userType(Role.PLAYER)
                .build();
    }

    private UserResponseDTO buildUserResponse() {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(1L);
        dto.setEmail("player@gmail.com");
        dto.setUserType(Role.PLAYER);
        return dto;
    }
}

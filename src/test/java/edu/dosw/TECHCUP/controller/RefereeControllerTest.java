package edu.dosw.TECHCUP.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.dosw.TECHCUP.controller.dto.request.UserRequestDTO;
import edu.dosw.TECHCUP.controller.dto.response.UserResponseDTO;
import edu.dosw.TECHCUP.controller.mapper.UserMapper;
import edu.dosw.TECHCUP.core.model.User;
import edu.dosw.TECHCUP.core.model.enums.Role;
import edu.dosw.TECHCUP.core.service.impl.RefereeService;
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
class RefereeControllerTest {

    MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();

    @Mock RefereeService refereeService;
    @Mock UserMapper userMapper;

    @InjectMocks RefereeController controller;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void createReferee_returns201() throws Exception {
        UserRequestDTO request = buildUserRequest();
        User model = buildUser();
        UserResponseDTO response = buildUserResponse();

        when(userMapper.toModel(any(UserRequestDTO.class))).thenReturn(model);
        when(refereeService.createUser(model)).thenReturn(model);
        when(userMapper.toDto(model)).thenReturn(response);

        mockMvc.perform(post("/api/referees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    void getAllReferees_returns200() throws Exception {
        User model = buildUser();
        UserResponseDTO response = buildUserResponse();

        when(refereeService.getAllReferees()).thenReturn(List.of(model));
        when(userMapper.toDto(model)).thenReturn(response);

        mockMvc.perform(get("/api/referees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void getRefereeById_returns200() throws Exception {
        User model = buildUser();
        UserResponseDTO response = buildUserResponse();

        when(refereeService.getRefereeById(1L)).thenReturn(model);
        when(userMapper.toDto(model)).thenReturn(response);

        mockMvc.perform(get("/api/referees/1"))
                .andExpect(status().isOk());
    }

    @Test
    void updateReferee_returns200() throws Exception {
        UserRequestDTO request = buildUserRequest();
        User model = buildUser();
        UserResponseDTO response = buildUserResponse();

        when(userMapper.toModel(any(UserRequestDTO.class))).thenReturn(model);
        when(refereeService.updateUser(eq(1L), any())).thenReturn(model);
        when(userMapper.toDto(model)).thenReturn(response);

        mockMvc.perform(put("/api/referees/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void deleteReferee_returns204() throws Exception {
        doNothing().when(refereeService).deleteUser(1L);

        mockMvc.perform(delete("/api/referees/1"))
                .andExpect(status().isNoContent());
    }

    // ─── Helpers ──────────────────────────────────────────────────────────────

    private UserRequestDTO buildUserRequest() {
        return UserRequestDTO.builder()
                .firstName("Carlos")
                .lastName("Gomez")
                .email("ref@gmail.com")
                .password("password123")
                .documentId("555666")
                .userType(Role.REFEREE)
                .build();
    }

    private User buildUser() {
        return User.builder()
                .userId(1L)
                .firstName("Carlos")
                .lastName("Gomez")
                .email("ref@gmail.com")
                .userType(Role.REFEREE)
                .build();
    }

    private UserResponseDTO buildUserResponse() {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(1L);
        dto.setEmail("ref@gmail.com");
        dto.setUserType(Role.REFEREE);
        return dto;
    }
}

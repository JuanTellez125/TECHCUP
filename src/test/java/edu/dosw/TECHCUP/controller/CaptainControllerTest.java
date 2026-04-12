package edu.dosw.TECHCUP.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.dosw.TECHCUP.controller.dto.request.UserRequestDTO;
import edu.dosw.TECHCUP.controller.dto.response.UserResponseDTO;
import edu.dosw.TECHCUP.controller.mapper.UserMapper;
import edu.dosw.TECHCUP.core.model.User;
import edu.dosw.TECHCUP.core.model.enums.Role;
import edu.dosw.TECHCUP.core.service.impl.CaptainService;
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
class CaptainControllerTest {

    MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();

    @Mock CaptainService captainService;
    @Mock UserMapper userMapper;

    @InjectMocks CaptainController controller;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void createCaptain_returns201() throws Exception {
        UserRequestDTO request = buildUserRequest();
        User model = buildUser();
        UserResponseDTO response = buildUserResponse();

        when(userMapper.toModel(any(UserRequestDTO.class))).thenReturn(model);
        when(captainService.createUser(model)).thenReturn(model);
        when(userMapper.toDto(model)).thenReturn(response);

        mockMvc.perform(post("/api/captains")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("captain@gmail.com"));
    }

    @Test
    void getCaptainById_returns200() throws Exception {
        User model = buildUser();
        UserResponseDTO response = buildUserResponse();

        when(captainService.getCaptainById(1L)).thenReturn(model);
        when(userMapper.toDto(model)).thenReturn(response);

        mockMvc.perform(get("/api/captains/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("captain@gmail.com"));
    }

    @Test
    void updateCaptain_returns200() throws Exception {
        UserRequestDTO request = buildUserRequest();
        User model = buildUser();
        UserResponseDTO response = buildUserResponse();

        when(userMapper.toModel(any(UserRequestDTO.class))).thenReturn(model);
        when(captainService.updateUser(eq(1L), any())).thenReturn(model);
        when(userMapper.toDto(model)).thenReturn(response);

        mockMvc.perform(put("/api/captains/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void deleteCaptain_returns204() throws Exception {
        doNothing().when(captainService).deleteUser(1L);

        mockMvc.perform(delete("/api/captains/1"))
                .andExpect(status().isNoContent());
    }

    // ─── Helpers ──────────────────────────────────────────────────────────────

    private UserRequestDTO buildUserRequest() {
        return UserRequestDTO.builder()
                .firstName("Pedro")
                .lastName("Ramirez")
                .email("captain@gmail.com")
                .password("password123")
                .documentId("789012")
                .userType(Role.CAPTAIN)
                .build();
    }

    private User buildUser() {
        return User.builder()
                .userId(1L)
                .firstName("Pedro")
                .lastName("Ramirez")
                .email("captain@gmail.com")
                .userType(Role.CAPTAIN)
                .build();
    }

    private UserResponseDTO buildUserResponse() {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(1L);
        dto.setFirstName("Pedro");
        dto.setLastName("Ramirez");
        dto.setEmail("captain@gmail.com");
        dto.setUserType(Role.CAPTAIN);
        return dto;
    }
}

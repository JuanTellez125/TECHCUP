package edu.dosw.TECHCUP.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.dosw.TECHCUP.controller.dto.request.UserRequestDTO;
import edu.dosw.TECHCUP.controller.dto.response.UserResponseDTO;
import edu.dosw.TECHCUP.controller.mapper.UserMapper;
import edu.dosw.TECHCUP.core.model.User;
import edu.dosw.TECHCUP.core.model.enums.Role;
import edu.dosw.TECHCUP.core.service.impl.OrganizerService;
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
class OrganizerControllerTest {

    MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();

    @Mock OrganizerService organizerService;
    @Mock UserMapper userMapper;

    @InjectMocks OrganizerController controller;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void createOrganizer_returns201() throws Exception {
        UserRequestDTO request = buildUserRequest();
        User model = buildUser();
        UserResponseDTO response = buildUserResponse();

        when(userMapper.toModel(any(UserRequestDTO.class))).thenReturn(model);
        when(organizerService.createUser(model)).thenReturn(model);
        when(userMapper.toDto(model)).thenReturn(response);

        mockMvc.perform(post("/api/organizers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    void getAllOrganizers_returns200() throws Exception {
        User model = buildUser();
        UserResponseDTO response = buildUserResponse();

        when(organizerService.getAllOrganizers()).thenReturn(List.of(model));
        when(userMapper.toDto(model)).thenReturn(response);

        mockMvc.perform(get("/api/organizers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void getOrganizerById_returns200() throws Exception {
        User model = buildUser();
        UserResponseDTO response = buildUserResponse();

        when(organizerService.getOrganizerById(1L)).thenReturn(model);
        when(userMapper.toDto(model)).thenReturn(response);

        mockMvc.perform(get("/api/organizers/1"))
                .andExpect(status().isOk());
    }

    @Test
    void updateOrganizer_returns200() throws Exception {
        UserRequestDTO request = buildUserRequest();
        User model = buildUser();
        UserResponseDTO response = buildUserResponse();

        when(userMapper.toModel(any(UserRequestDTO.class))).thenReturn(model);
        when(organizerService.updateUser(eq(1L), any())).thenReturn(model);
        when(userMapper.toDto(model)).thenReturn(response);

        mockMvc.perform(put("/api/organizers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void deleteOrganizer_returns204() throws Exception {
        doNothing().when(organizerService).deleteUser(1L);

        mockMvc.perform(delete("/api/organizers/1"))
                .andExpect(status().isNoContent());
    }

    // ─── Helpers ──────────────────────────────────────────────────────────────

    private UserRequestDTO buildUserRequest() {
        return UserRequestDTO.builder()
                .firstName("Ana")
                .lastName("Lopez")
                .email("organizer@gmail.com")
                .password("password123")
                .documentId("333444")
                .userType(Role.ORGANIZER)
                .build();
    }

    private User buildUser() {
        return User.builder()
                .userId(1L)
                .firstName("Ana")
                .lastName("Lopez")
                .email("organizer@gmail.com")
                .userType(Role.ORGANIZER)
                .build();
    }

    private UserResponseDTO buildUserResponse() {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(1L);
        dto.setEmail("organizer@gmail.com");
        dto.setUserType(Role.ORGANIZER);
        return dto;
    }
}

package edu.dosw.TECHCUP.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.dosw.TECHCUP.controller.dto.request.UserRequestDTO;
import edu.dosw.TECHCUP.controller.dto.response.UserResponseDTO;
import edu.dosw.TECHCUP.controller.mapper.UserMapper;
import edu.dosw.TECHCUP.core.model.User;
import edu.dosw.TECHCUP.core.model.enums.Role;
import edu.dosw.TECHCUP.core.service.impl.AdministratorService;
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
class AdministratorControllerTest {

    MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();

    @Mock AdministratorService administratorService;
    @Mock UserMapper userMapper;

    @InjectMocks AdministratorController controller;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void createAdministrator_returns201() throws Exception {
        UserRequestDTO request = buildUserRequest();
        User model = buildUser();
        UserResponseDTO response = buildUserResponse();

        when(userMapper.toModel(any(UserRequestDTO.class))).thenReturn(model);
        when(administratorService.createUser(model)).thenReturn(model);
        when(userMapper.toDto(model)).thenReturn(response);

        mockMvc.perform(post("/api/administrators")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("juan@gmail.com"));
    }

    @Test
    void getAllUsers_returns200() throws Exception {
        User model = buildUser();
        UserResponseDTO response = buildUserResponse();

        when(administratorService.getAllUsers()).thenReturn(List.of(model));
        when(userMapper.toDto(model)).thenReturn(response);

        mockMvc.perform(get("/api/administrators"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void getAdminById_returns200() throws Exception {
        User model = buildUser();
        UserResponseDTO response = buildUserResponse();

        when(administratorService.getAdminById(1L)).thenReturn(model);
        when(userMapper.toDto(model)).thenReturn(response);

        mockMvc.perform(get("/api/administrators/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("juan@gmail.com"));
    }

    @Test
    void updateAdministrator_returns200() throws Exception {
        UserRequestDTO request = buildUserRequest();
        User model = buildUser();
        UserResponseDTO response = buildUserResponse();

        when(userMapper.toModel(any(UserRequestDTO.class))).thenReturn(model);
        when(administratorService.updateUser(eq(1L), any())).thenReturn(model);
        when(userMapper.toDto(model)).thenReturn(response);

        mockMvc.perform(put("/api/administrators/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void deleteAdministrator_returns204() throws Exception {
        doNothing().when(administratorService).deleteUser(1L);

        mockMvc.perform(delete("/api/administrators/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void assignRole_returns200() throws Exception {
        User model = buildUser();
        UserResponseDTO response = buildUserResponse();

        when(administratorService.assignRole(1L, 2L, Role.CAPTAIN)).thenReturn(model);
        when(userMapper.toDto(model)).thenReturn(response);

        mockMvc.perform(patch("/api/administrators/1/assign-role")
                        .param("targetUserId", "2")
                        .param("newRole", "CAPTAIN"))
                .andExpect(status().isOk());
    }

    // ─── Helpers ──────────────────────────────────────────────────────────────

    private UserRequestDTO buildUserRequest() {
        return UserRequestDTO.builder()
                .firstName("Juan")
                .lastName("Tellez")
                .email("juan@gmail.com")
                .password("password123")
                .documentId("123456")
                .userType(Role.ADMINISTRATOR)
                .build();
    }

    private User buildUser() {
        return User.builder()
                .userId(1L)
                .firstName("Juan")
                .lastName("Tellez")
                .email("juan@gmail.com")
                .userType(Role.ADMINISTRATOR)
                .build();
    }

    private UserResponseDTO buildUserResponse() {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(1L);
        dto.setFirstName("Juan");
        dto.setLastName("Tellez");
        dto.setEmail("juan@gmail.com");
        dto.setUserType(Role.ADMINISTRATOR);
        return dto;
    }
}

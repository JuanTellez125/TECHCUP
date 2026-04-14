package edu.dosw.TECHCUP.controller;

import edu.dosw.TECHCUP.controller.mapper.UserMapper;
import edu.dosw.TECHCUP.core.exception.InvalidAdminException;
import edu.dosw.TECHCUP.core.exception.UnauthorizedRoleAssignmentException;
import edu.dosw.TECHCUP.core.exception.UserNotFoundException;
import edu.dosw.TECHCUP.core.model.User;
import edu.dosw.TECHCUP.core.model.enums.Role;
import edu.dosw.TECHCUP.controller.dto.response.UserResponseDTO;
import edu.dosw.TECHCUP.core.service.impl.AdministratorService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = AdministratorController.class,
        excludeAutoConfiguration = {SecurityAutoConfiguration.class, SecurityFilterAutoConfiguration.class}
)
class AssignRoleIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    AdministratorService administratorService;

    @MockBean
    UserMapper userMapper;

    // ─── PATCH /api/administrators/{adminId}/assign-role ─────────────────────

    @Test
    void assignRole_success_returns200() throws Exception {
        User model = buildUser(Role.CAPTAIN);
        UserResponseDTO response = buildResponse(Role.CAPTAIN);

        when(administratorService.assignRole(1L, 2L, Role.CAPTAIN)).thenReturn(model);
        when(userMapper.toDto(model)).thenReturn(response);

        mockMvc.perform(patch("/api/administrators/1/assign-role")
                        .param("targetUserId", "2")
                        .param("newRole", "CAPTAIN"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userType").value("CAPTAIN"));
    }

    @Test
    void assignRole_invalidAdmin_returns403() throws Exception {
        when(administratorService.assignRole(99L, 2L, Role.PLAYER))
                .thenThrow(new InvalidAdminException(99L));

        mockMvc.perform(patch("/api/administrators/99/assign-role")
                        .param("targetUserId", "2")
                        .param("newRole", "PLAYER"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.status").value(403))
                .andExpect(jsonPath("$.message").value(
                        "User with id 99 does not have ADMINISTRATOR role."));
    }

    @Test
    void assignRole_newRoleIsAdministrator_returns403() throws Exception {
        when(administratorService.assignRole(1L, 2L, Role.ADMINISTRATOR))
                .thenThrow(new UnauthorizedRoleAssignmentException());

        mockMvc.perform(patch("/api/administrators/1/assign-role")
                        .param("targetUserId", "2")
                        .param("newRole", "ADMINISTRATOR"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.status").value(403))
                .andExpect(jsonPath("$.message").value(
                        "Assigning the ADMINISTRATOR role is not allowed through this endpoint."));
    }

    @Test
    void assignRole_targetNotFound_returns404() throws Exception {
        when(administratorService.assignRole(1L, 99L, Role.PLAYER))
                .thenThrow(new UserNotFoundException(99L));

        mockMvc.perform(patch("/api/administrators/1/assign-role")
                        .param("targetUserId", "99")
                        .param("newRole", "PLAYER"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    // ─── Helpers ──────────────────────────────────────────────────────────────

    private User buildUser(Role role) {
        return User.builder()
                .userId(2L)
                .firstName("Carlos")
                .lastName("Lopez")
                .email("carlos@gmail.com")
                .userType(role)
                .build();
    }

    private UserResponseDTO buildResponse(Role role) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(2L);
        dto.setFirstName("Carlos");
        dto.setLastName("Lopez");
        dto.setEmail("carlos@gmail.com");
        dto.setUserType(role);
        return dto;
    }
}

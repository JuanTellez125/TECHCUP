package edu.dosw.TECHCUP.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CurrentUserResponseDTO {
    private Long id;
    private String email;
    private String name;
    private String role;
    /** ID del equipo al que pertenece el usuario. Null si no tiene equipo. */
    private Long teamId;
}

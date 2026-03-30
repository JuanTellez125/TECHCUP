package edu.dosw.TECHCUP.controller.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InvitationRequestDTO {

    @NotNull(message = "El ID del equipo no puede estar vacío")
    private Long teamId;

    @NotNull(message = "El ID del jugador invitado no puede estar vacío")
    private Long invitedUserId;
}
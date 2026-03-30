package edu.dosw.TECHCUP.controller.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TournamentRegistrationRequestDTO {

    @NotNull(message = "El ID del torneo no puede estar vacío")
    private Long tournamentId;

    @NotNull(message = "El ID del equipo no puede estar vacío")
    private Long teamId;
}
package edu.dosw.TECHCUP.controller.dto.request;

import edu.dosw.TECHCUP.core.model.enums.Event;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MatchEventRequestDTO {

    @NotNull(message = "El ID del partido no puede estar vacío")
    private Long matchId;

    @NotNull(message = "El ID del jugador no puede estar vacío")
    private Long userId;

    @NotNull(message = "El ID del equipo no puede estar vacío")
    private Long teamId;

    @NotBlank(message = "El tipo de evento no puede estar vacío")
    private Event eventType;

    @Positive(message = "El minuto debe ser mayor a 0")
    private int minute;
}
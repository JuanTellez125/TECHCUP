package edu.dosw.TECHCUP.controller.dto.request;

import edu.dosw.TECHCUP.core.model.enums.LineUpRole;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LineUpRequestDTO {

    @NotNull(message = "El ID del partido no puede estar vacío")
    private Long matchId;

    @NotNull(message = "El ID del equipo no puede estar vacío")
    private Long teamId;

    @NotNull(message = "El ID del jugador no puede estar vacío")
    private Long userId;

    @NotNull(message = "El rol no puede estar vacío")
    private LineUpRole role;

    private String position;

    private int jerseyNumber;
}
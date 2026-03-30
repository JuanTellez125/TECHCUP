package edu.dosw.TECHCUP.controller.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MatchResultRequestDTO {

    @NotNull(message = "El ID del partido no puede estar vacío")
    private Long matchId;

    @PositiveOrZero(message = "Los goles no pueden ser negativos")
    private int team1Goals;

    @PositiveOrZero(message = "Los goles no pueden ser negativos")
    private int team2Goals;
}
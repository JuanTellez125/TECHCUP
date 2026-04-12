package edu.dosw.TECHCUP.controller.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MatchResultRequestDTO {

    @NotNull(message = "The match ID cannot be empty")
    private Long matchId;

    @PositiveOrZero(message = "Goals cannot be negative")
    private int team1Goals;

    @PositiveOrZero(message = "Goals cannot be negative")
    private int team2Goals;
}
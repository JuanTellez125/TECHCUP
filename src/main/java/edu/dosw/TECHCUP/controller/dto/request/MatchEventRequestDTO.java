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

    @NotNull(message = "The match ID cannot be empty")
    private Long matchId;

    @NotNull(message = "The player ID cannot be empty")
    private Long userId;

    @NotNull(message = "The team ID cannot be empty.")
    private Long teamId;

    @NotBlank(message = "The event type cannot be empty")
    private Event eventType;

    @Positive(message = "The minute must be greater than 0")
    private int minute;
}
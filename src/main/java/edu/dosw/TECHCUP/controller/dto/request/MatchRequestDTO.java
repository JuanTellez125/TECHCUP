package edu.dosw.TECHCUP.controller.dto.request;

import edu.dosw.TECHCUP.core.model.enums.MatchPhase;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MatchRequestDTO {

    @NotNull(message = "The tournament ID cannot be empty.")
    private Long tournamentId;

    @NotNull(message = "Team 1 cannot be empty")
    private Long team1Id;

    @NotNull(message = "Team 2 cannot be empty")
    private Long team2Id;

    @NotNull(message = "The court cannot be empty.")
    private Long venueId;

    private Long refereeId;

    @NotNull(message = "The match phase cannot be empty")
    private MatchPhase phase;

    @NotNull(message = "The match date cannot be blank.")
    private LocalDateTime scheduledAt;
}
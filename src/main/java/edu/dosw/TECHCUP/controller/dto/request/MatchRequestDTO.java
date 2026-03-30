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

    @NotNull(message = "El ID del torneo no puede estar vacío")
    private Long tournamentId;

    @NotNull(message = "El equipo 1 no puede estar vacío")
    private Long team1Id;

    @NotNull(message = "El equipo 2 no puede estar vacío")
    private Long team2Id;

    @NotNull(message = "La cancha no puede estar vacía")
    private Long venueId;

    private Long refereeId;

    @NotNull(message = "La fase del partido no puede estar vacía")
    private MatchPhase phase;

    @NotNull(message = "La fecha del partido no puede estar vacía")
    private LocalDateTime scheduledAt;
}
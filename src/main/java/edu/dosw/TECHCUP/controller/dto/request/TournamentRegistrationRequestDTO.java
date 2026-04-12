package edu.dosw.TECHCUP.controller.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TournamentRegistrationRequestDTO {

    @NotNull(message = "The tournament ID cannot be empty.")
    private Long tournamentId;

    @NotNull(message = "The team ID cannot be empty.")
    private Long teamId;
}
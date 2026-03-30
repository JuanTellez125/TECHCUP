package edu.dosw.TECHCUP.controller.dto.response;

import edu.dosw.TECHCUP.core.model.enums.RegisterTournamentStatus;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TournamentRegistrationResponseDTO {

    private Long id;

    private Long tournamentId;

    private Long teamId;

    private String teamName;

    private RegisterTournamentStatus status;

    private LocalDateTime registeredAt;
}
package edu.dosw.TECHCUP.core.model;

import edu.dosw.TECHCUP.core.model.enums.RegisterTournamentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TournamentRegistration {
    private Long tournamentRegistrationId;
    private Tournament tournament;
    private Team team;
    private RegisterTournamentStatus status;
    private LocalDateTime registeredAt;
}

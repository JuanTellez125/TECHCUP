package edu.dosw.TECHCUP.controller.dto.response;

import edu.dosw.TECHCUP.core.model.TournamentStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TournamentResponseDTO {

    private String id;

    private LocalDate startDate;

    private LocalDate endDate;

    private int totalTeams;

    private double registrationCost;

    private TournamentStatus status;
}

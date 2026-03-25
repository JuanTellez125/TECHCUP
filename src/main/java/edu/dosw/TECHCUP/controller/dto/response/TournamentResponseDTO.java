package edu.dosw.TECHCUP.controller.dto.response;

import edu.dosw.TECHCUP.core.model.enums.TournamentStatus;
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

    private String rules;

    private LocalDate registrationCloseDate;

    private String fields;

    private String sanctions;
}

package edu.dosw.TECHCUP.controller.dto.request;

import edu.dosw.TECHCUP.core.model.enums.TournamentStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TournamentRequestDTO {

    @NotNull (message = "The start date cannot be blank")
    private LocalDate startDate;

    @NotNull (message = "The end date cannot be blank")
    private LocalDate endDate;

    @NotNull (message = "Total teams cannot be blank")
    private int totalTeams;

    @NotNull (message = "The registration cost cannot be blank")
    private double registrationCost;

    @NotNull (message = "The tournament's status cannot be blank")
    private TournamentStatus status;

}

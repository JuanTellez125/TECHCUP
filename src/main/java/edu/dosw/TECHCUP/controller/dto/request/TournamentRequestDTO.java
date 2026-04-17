package edu.dosw.TECHCUP.controller.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TournamentRequestDTO {

    private String name;

    @NotNull(message = "The start date cannot be empty.")
    private LocalDate startDate;

    @NotNull(message = "The end date cannot be empty.")
    private LocalDate endDate;

    @Positive(message = "The number of teams must be greater than 0")
    private int totalTeams;

    @Positive(message = "The registration fee must be greater than 0")
    private double registrationCost;
}
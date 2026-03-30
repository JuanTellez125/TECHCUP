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

    @NotNull(message = "La fecha de inicio no puede estar vacía")
    private LocalDate startDate;

    @NotNull(message = "La fecha de fin no puede estar vacía")
    private LocalDate endDate;

    @Positive(message = "El número de equipos debe ser mayor a 0")
    private int totalTeams;

    @Positive(message = "El costo de inscripción debe ser mayor a 0")
    private double registrationCost;
}
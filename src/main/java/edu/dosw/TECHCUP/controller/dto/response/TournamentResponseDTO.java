package edu.dosw.TECHCUP.controller.dto.response;

import edu.dosw.TECHCUP.core.model.enums.TournamentStatus;
import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TournamentResponseDTO {

    private Long id;

    private Long organizerId;

    private String organizerName;

    private LocalDate startDate;

    private LocalDate endDate;

    private int totalTeams;

    private double registrationCost;

    private TournamentStatus status;
}
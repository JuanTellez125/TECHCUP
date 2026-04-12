package edu.dosw.TECHCUP.core.model;

import edu.dosw.TECHCUP.core.model.enums.TournamentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Tournament {
    private Long tournamentId;
    private LocalDate startDate;
    private LocalDate endDate;
    private int totalTeams;
    private double registrationCost;
    private TournamentStatus status;
    private User organizer;
    private TournamentConfig config;
}
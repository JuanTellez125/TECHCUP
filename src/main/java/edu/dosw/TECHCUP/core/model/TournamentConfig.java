package edu.dosw.TECHCUP.core.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TournamentConfig {
    private Long tournamentConfigId;
    private Tournament tournament;
    private String rulebook;
    private LocalDate inscriptionDeadline;
    private String schedules;
    private String sanctions;
}

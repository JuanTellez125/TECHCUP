package edu.dosw.TECHCUP.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStatsResponseDTO {
    private int matchesPlayed;
    private int totalMatches;
    private int totalGoals;
    private int yellowCards;
    private Long tournamentId;
}

package edu.dosw.TECHCUP.controller.dto.response;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TournamentHistoryResponseDTO {

    private Long tournamentId;

    private LocalDate startDate;

    private LocalDate endDate;

    private String champion;

    private List<TeamResponseDTO> participants;

    private List<StandingResponseDTO> finalStandings;

    private List<TopScorerResponseDTO> topScorers;
}

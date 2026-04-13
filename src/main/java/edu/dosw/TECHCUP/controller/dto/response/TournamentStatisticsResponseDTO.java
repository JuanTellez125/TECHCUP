package edu.dosw.TECHCUP.controller.dto.response;

import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TournamentStatisticsResponseDTO {

    private List<StandingResponseDTO> standings;

    private List<TopScorerResponseDTO> topScorers;

    private List<MatchResponseDTO> matchHistory;
}
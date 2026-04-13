package edu.dosw.TECHCUP.controller.dto.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatchSummaryResponseDTO {

    private Long matchId;

    private String team1Name;

    private int team1Goals;

    private String team2Name;

    private int team2Goals;

    private LocalDateTime registeredAt;

    private List<MatchEventResponseDTO> events;

}
package edu.dosw.TECHCUP.controller.dto.response;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StandingResponseDTO {

    private Long id;

    private Long tournamentId;

    private Long teamId;

    private String teamName;

    private int played;

    private int won;

    private int drawn;

    private int lost;

    private int goalsFor;

    private int goalsAgainst;

    private int points;
}
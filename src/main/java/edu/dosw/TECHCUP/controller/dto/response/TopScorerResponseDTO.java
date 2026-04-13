package edu.dosw.TECHCUP.controller.dto.response;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TopScorerResponseDTO {

    private Long userId;

    private String playerName;

    private Long teamId;

    private String teamName;

    private int totalGoals;
}
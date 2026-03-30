package edu.dosw.TECHCUP.controller.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatchResultResponseDTO {

    private Long id;

    private Long matchId;

    private int team1Goals;

    private int team2Goals;

    private LocalDateTime registeredAt;
}
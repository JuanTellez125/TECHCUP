package edu.dosw.TECHCUP.controller.dto.response;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatchEventResponseDTO {

    private Long id;

    private Long matchId;

    private Long userId;

    private String userName;

    private Long teamId;

    private String teamName;

    private String eventType;

    private int minute;
}
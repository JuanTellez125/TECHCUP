package edu.dosw.TECHCUP.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeamMemberUpdateResponseDTO {
    private Long memberId;
    private Long playerId;
    private String playerName;
    private boolean suspended;
    private String position;
}

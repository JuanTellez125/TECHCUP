package edu.dosw.TECHCUP.controller.dto.response;

import edu.dosw.TECHCUP.core.model.enums.MatchPhase;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatchResponseDTO {

    private Long id;

    private Long tournamentId;

    private Long team1Id;

    private String team1Name;

    private Long team2Id;

    private String team2Name;

    private Long venueId;

    private String venueName;

    private Long refereeId;

    private String refereeName;

    private MatchPhase phase;

    private LocalDateTime scheduledAt;

    private MatchPhase status;
}
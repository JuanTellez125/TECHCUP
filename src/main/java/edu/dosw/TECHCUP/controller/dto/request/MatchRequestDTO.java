package edu.dosw.TECHCUP.controller.dto.request;

import edu.dosw.TECHCUP.core.model.enums.MatchPhase;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MatchRequestDTO {

    private String refereeId;

    private String localTeamId;

    private String awayTeamId;

    private int numberField;

    private MatchPhase matchPhase;

    private LocalDateTime matchDateTime;

    private String tournamentId;
}
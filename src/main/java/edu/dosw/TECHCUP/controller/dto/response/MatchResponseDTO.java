package edu.dosw.TECHCUP.controller.dto.response;

import edu.dosw.TECHCUP.core.model.Team;
import edu.dosw.TECHCUP.core.model.enums.MatchPhase;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatchResponseDTO {

    private String id;

    private String refereeId;

    private Team localTeam;

    private Team awayTeam;

    private int numberField;

    private MatchPhase matchPhase;

    private LocalDateTime matchDateTime;

    private boolean isPlayed;

    private int localScore;

    private int awayScore;

    private List<String> scorerIds;

    private List<String> yellowCardPlayerIds;

    private List<String> redCardPlayerIds;
}
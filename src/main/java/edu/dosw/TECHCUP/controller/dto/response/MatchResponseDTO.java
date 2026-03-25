package edu.dosw.TECHCUP.controller.dto.response;

import edu.dosw.TECHCUP.core.model.Team;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatchResponseDTO {

    private String id;

    private String referee_id;

    private Team localTeam;

    private Team awayTeam;

    private int numberField;

}

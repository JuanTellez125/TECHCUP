package edu.dosw.TECHCUP.controller.dto.request;

import edu.dosw.TECHCUP.core.model.Team;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MatchRequestDTO {

    private String referee_id;

    private Team localTeam;

    private Team awayTeam;

    private int numberField;

}

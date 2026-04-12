package edu.dosw.TECHCUP.core.model;

import edu.dosw.TECHCUP.core.model.enums.LineUpRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LineUp {
    private Long lineUpId;
    private Match match;
    private Team team;
    private User user;
    private LineUpRole role;
    private String position;
    private int jerseyNumber;
}

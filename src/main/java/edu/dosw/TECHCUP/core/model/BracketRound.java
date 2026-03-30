package edu.dosw.TECHCUP.core.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BracketRound {
    private Long BracketRound_id;
    private Tournament tournament;
    private String roundName;
}

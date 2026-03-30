package edu.dosw.TECHCUP.core.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MatchResult {
    private long MatchResult_id;
    private Match match;
    private User registeredBy;
    private int team1Goals;
    private int team2Goals;
    private LocalDateTime registeredAt;
}

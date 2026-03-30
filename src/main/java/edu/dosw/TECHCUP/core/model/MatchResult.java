package edu.dosw.TECHCUP.core.model;

import java.time.LocalDateTime;

public class MatchResult {
    private long MatchResult_id;
    private Match match;
    private User registeredBy;
    private int team1Goals;
    private int team2Goals;
    private LocalDateTime registeredAt;
}

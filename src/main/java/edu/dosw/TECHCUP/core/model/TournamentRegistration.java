package edu.dosw.TECHCUP.core.model;

import edu.dosw.TECHCUP.core.model.enums.RegisterTournamentStatus;

import java.time.LocalDateTime;

public class TournamentRegistration {
    private Long TournamentRegistration_id;
    private Tournament tournament;
    private Team team;
    private RegisterTournamentStatus status;
    private LocalDateTime registeredAt;
}

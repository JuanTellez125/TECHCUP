package edu.dosw.TECHCUP.persistence.entity;

import edu.dosw.TECHCUP.core.model.Match;
import edu.dosw.TECHCUP.core.model.Team;
import edu.dosw.TECHCUP.core.model.User;
import edu.dosw.TECHCUP.core.model.enums.Event;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "match_events")
public class MatchEventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long MatchEvent_id;

    @ManyToOne
    @JoinColumn(name = "match_id")
    private MatchEntity match;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private TeamEntity team;

    @Enumerated(EnumType.STRING)
    private Event eventType;

    private int minute;
}

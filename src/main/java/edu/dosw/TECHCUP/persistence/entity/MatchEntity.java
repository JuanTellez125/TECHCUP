package edu.dosw.TECHCUP.persistence.entity;

import edu.dosw.TECHCUP.core.model.*;
import edu.dosw.TECHCUP.core.model.enums.MatchPhase;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "matches")
public class MatchEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long match_id;

    @ManyToOne
    @JoinColumn(name = "tournament_id")
    private Tournament tournament;

    @ManyToOne
    @JoinColumn(name = "team1_id")
    private Team team1;

    @ManyToOne
    @JoinColumn(name = "team2_id")
    private Team team2;

    @ManyToOne
    @JoinColumn(name = "venue_id")
    private Venue vanue;

    @ManyToOne
    @JoinColumn(name = "referee_id")
    private User referee;

    @Enumerated(EnumType.STRING)
    private MatchPhase phase;

    private LocalDateTime scheduledAt;
    private String status;

    @OneToOne(mappedBy = "match", cascade = CascadeType.ALL)
    private MatchResult result;

    @OneToMany(mappedBy = "match", cascade = CascadeType.ALL)
    private List<MatchEvent> events;

    @OneToMany(mappedBy = "match", cascade = CascadeType.ALL)
    private List<LineUp> lineups;
}

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
    private TournamentEntity tournament;

    @ManyToOne
    @JoinColumn(name = "team1_id")
    private TeamEntity team1;

    @ManyToOne
    @JoinColumn(name = "team2_id")
    private TeamEntity team2;

    @ManyToOne
    @JoinColumn(name = "venue_id")
    private VenueEntity vanue;

    @ManyToOne
    @JoinColumn(name = "referee_id")
    private UserEntity referee;

    @Enumerated(EnumType.STRING)
    private MatchPhase phase;

    private LocalDateTime scheduledAt;
    private String status;

    @OneToOne(mappedBy = "match", cascade = CascadeType.ALL)
    private MatchResultEntity result;

    @OneToMany(mappedBy = "match", cascade = CascadeType.ALL)
    private List<MatchEventEntity> events;

    @OneToMany(mappedBy = "match", cascade = CascadeType.ALL)
    private List<LineUpEntity> lineups;
}

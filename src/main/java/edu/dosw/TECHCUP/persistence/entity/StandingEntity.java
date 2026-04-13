package edu.dosw.TECHCUP.persistence.entity;

import edu.dosw.TECHCUP.core.model.Team;
import edu.dosw.TECHCUP.core.model.Tournament;
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
@Table(name = "standings")
public class StandingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long standing_id;

    @ManyToOne
    @JoinColumn(name = "tournament_id")
    private TournamentEntity tournament;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private TeamEntity team;

    private int played;
    private int won;
    private int drawn;
    private int lost;
    private int goalsFor;
    private int goalsAgainst;

    @Column(name = "goal_difference")
    private int goalDifference;

    private int points;
}

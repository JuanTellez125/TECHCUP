package edu.dosw.TECHCUP.persistence.entity;

import edu.dosw.TECHCUP.core.model.Match;
import edu.dosw.TECHCUP.core.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "match_results")
public class MatchResultEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long MatchResult_id;

    @OneToOne
    @JoinColumn(name = "match_id")
    private MatchEntity match;

    @ManyToOne
    @JoinColumn(name = "registered_by")
    private UserEntity registeredBy;

    private int team1Goals;
    private int team2Goals;
    private LocalDateTime registeredAt;
}

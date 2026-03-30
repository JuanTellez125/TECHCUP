package edu.dosw.TECHCUP.persistence.entity;

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
@Table(name = "bracket_rounds")
public class BracketRoundEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long BracketRound_id;

    @ManyToOne
    @JoinColumn(name = "tournament_id")
    private Tournament tournament;

    private String roundName;
}

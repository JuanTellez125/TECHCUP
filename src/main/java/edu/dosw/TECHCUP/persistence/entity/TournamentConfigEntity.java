package edu.dosw.TECHCUP.persistence.entity;

import edu.dosw.TECHCUP.core.model.Tournament;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tournament_config")
public class TournamentConfigEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tournamentConfig_id;

    @OneToOne
    @JoinColumn(name = "tournament_id")
    private Tournament tournament;

    private String rulebook;
    private LocalDate inscriptionDeadline;
    private String schedules;
    private String sanctions;
}

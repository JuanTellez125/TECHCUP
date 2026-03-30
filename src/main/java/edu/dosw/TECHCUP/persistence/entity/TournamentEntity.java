package edu.dosw.TECHCUP.persistence.entity;

import edu.dosw.TECHCUP.core.model.TournamentConfig;
import edu.dosw.TECHCUP.core.model.User;
import edu.dosw.TECHCUP.core.model.enums.TournamentStatus;
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
@Table(name = "tournaments")
public class TournamentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tournament_id;

    private LocalDate startDate;
    private LocalDate endDate;
    private int totalTeams;
    private double registrationCost;

    @Enumerated(EnumType.STRING)
    private TournamentStatus status;

    @ManyToOne
    @JoinColumn(name = "organizer_id")
    private UserEntity organizer;

    @OneToOne(mappedBy = "tournament", cascade = CascadeType.ALL)
    private TournamentConfigEntity config;
}

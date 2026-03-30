package edu.dosw.TECHCUP.persistence.entity;

import edu.dosw.TECHCUP.core.model.Team;
import edu.dosw.TECHCUP.core.model.Tournament;
import edu.dosw.TECHCUP.core.model.enums.RegisterTournamentStatus;
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
@Table(name = "tournament_registrations")
public class TournamentRegistrationEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tournamentRegistration_id;

    @ManyToOne
    @JoinColumn(name = "tournament_id")
    private TournamentEntity tournament;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private TeamEntity team;

    @Enumerated(EnumType.STRING)
    private RegisterTournamentStatus status;

    private LocalDateTime registeredAt;
}

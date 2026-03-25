package edu.dosw.TECHCUP.core.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import edu.dosw.TECHCUP.core.model.enums.TournamentStatus;

import java.time.LocalDate;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tournaments")
@Entity
public class Tournament {

    @Id
    private String id;

    private LocalDate startDate;

    private LocalDate endDate;

    private int totalTeams;

    private double registrationCost;

    private TournamentStatus status;

    private String organizerId;

    private String rules;

    private LocalDate registrationCloseDate;

    private String fields;

    private String sanctions;

}
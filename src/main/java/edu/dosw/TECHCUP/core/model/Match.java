package edu.dosw.TECHCUP.core.model;


import edu.dosw.TECHCUP.core.model.enums.MatchPhase;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.awt.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name="matches")
@Entity
public class Match{

    @Id
    private String id;

    private String refereeIid;

    private Team localTeam;

    private Team awayTeam;

    private int numberField;

    private MatchPhase matchPhase;

    private Tournament tournament;

    private boolean isPlayed;

    private int localScore;

    private int awayScore;

    private LocalDateTime matchDateTime;

    private List<String> scorerIds;

    private List<String> yellowCardPlayerIds;

    private List<String> redCardPlayerIds;

}
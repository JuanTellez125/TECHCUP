package edu.dosw.TECHCUP.core.model;


import edu.dosw.TECHCUP.core.model.enums.MatchPhase;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import javax.sound.sampled.Line;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Match{
    private Long match_id;
    private Tournament tournament;
    private Team team1;
    private Team team2;
    private Venue vanue;
    private User referee;
    private MatchPhase phase;
    private LocalDateTime scheduledAt;
    private String status;
    private MatchResult result;
    private List<MatchEvent> events;
//    private  List<LineUp> lineups;
}
package edu.dosw.TECHCUP.core.model;

import edu.dosw.TECHCUP.core.model.enums.Event;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MatchEvent {
    private Long matchEventId;
    private Match match;
    private User user;
    private Team team;
    private Event eventType;
    private int minute;
}

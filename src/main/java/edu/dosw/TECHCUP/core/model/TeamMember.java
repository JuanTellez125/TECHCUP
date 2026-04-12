package edu.dosw.TECHCUP.core.model;

import edu.dosw.TECHCUP.core.model.enums.TeamMemberStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TeamMember {
    private Long teamMemberId;
    private Team team;
    private User user;
    private TeamMemberStatus status;
    private boolean available;
}

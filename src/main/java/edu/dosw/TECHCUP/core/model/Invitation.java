package edu.dosw.TECHCUP.core.model;

import edu.dosw.TECHCUP.core.model.enums.InvitationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Invitation {
    private Long invitationId;
    private Team team;
    private User invitedUser;
    private User invitedBy;
    private InvitationStatus status;
}

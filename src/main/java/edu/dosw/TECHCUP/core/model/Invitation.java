package edu.dosw.TECHCUP.core.model;

import edu.dosw.TECHCUP.core.model.enums.InvitationStatus;

public class Invitation {
    private long invitation_id;
    private Team team;
    private User invitedUser;
    private User invitedBy;
    private InvitationStatus status;
}

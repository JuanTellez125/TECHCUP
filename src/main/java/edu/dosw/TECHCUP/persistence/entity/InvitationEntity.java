package edu.dosw.TECHCUP.persistence.entity;

import edu.dosw.TECHCUP.core.model.Team;
import edu.dosw.TECHCUP.core.model.User;
import edu.dosw.TECHCUP.core.model.enums.InvitationStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "invitations")
public class InvitationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long invitation_id;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private TeamEntity team;

    @ManyToOne
    @JoinColumn(name = "invited_user_id")
    private UserEntity invitedUser;

    @ManyToOne
    @JoinColumn(name = "invited_by")
    private UserEntity invitedBy;

    @Enumerated(EnumType.STRING)
    private InvitationStatus status;
}

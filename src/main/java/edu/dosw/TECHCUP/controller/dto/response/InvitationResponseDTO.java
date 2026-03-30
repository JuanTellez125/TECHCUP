package edu.dosw.TECHCUP.controller.dto.response;

import edu.dosw.TECHCUP.core.model.enums.InvitationStatus;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvitationResponseDTO {

    private Long id;

    private Long teamId;

    private String teamName;

    private Long invitedUserId;

    private String invitedUserName;

    private Long invitedById;

    private String invitedByName;

    private InvitationStatus status;
}
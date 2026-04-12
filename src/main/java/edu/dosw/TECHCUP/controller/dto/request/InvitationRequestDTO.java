package edu.dosw.TECHCUP.controller.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InvitationRequestDTO {

    @NotNull(message = "The team ID cannot be empty")
    private Long teamId;

    @NotNull(message = "The Guest Player ID cannot be empty")
    private Long invitedUserId;
}
package edu.dosw.TECHCUP.controller.dto.request;

import edu.dosw.TECHCUP.core.model.enums.LineUpRole;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LineUpRequestDTO {

    @NotNull(message = "The match ID cannot be empty")
    private Long matchId;

    @NotNull(message = "The team ID cannot be empty.")
    private Long teamId;

    @NotNull(message = "The player ID cannot be empty")
    private Long userId;

    @NotNull(message = "The role cannot be empty.")
    private LineUpRole role;

    private String position;

    private int jerseyNumber;
}
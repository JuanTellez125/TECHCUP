package edu.dosw.TECHCUP.controller.dto.request;

import edu.dosw.TECHCUP.core.model.enums.LineUpRole;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LineUpEntryDTO {

    @NotNull(message = "The player ID cannot be empty")
    private Long userId;

    @NotNull(message = "The role cannot be empty")
    private LineUpRole role;

    private String position;

    private int jerseyNumber;

}

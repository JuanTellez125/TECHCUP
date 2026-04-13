package edu.dosw.TECHCUP.controller.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LineUpRequestDTO {

    @NotNull(message = "The team ID cannot be empty.")
    private Long teamId;

    @NotEmpty(message = "La alineación no puede estar vacía")
    private List<LineUpEntryDTO> players;
}
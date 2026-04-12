package edu.dosw.TECHCUP.controller.dto.request;

import edu.dosw.TECHCUP.core.model.enums.Position;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SportProfileRequestDTO {

    @NotNull(message = "The primary position cannot be empty.")
    private Position primaryPosition;

    private Position secondaryPosition;

    private int jerseyNumber;

    @NotNull(message = "The photo cannot be empty.")
    private String photoUrl;

    @NotNull(message = "The birthdate cannot be empty.")
    private LocalDate birthDate;

    @NotNull(message = "Available cannot be empty.")
    private boolean available;
}
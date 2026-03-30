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

    @NotNull(message = "La posición principal no puede estar vacía")
    private Position primaryPosition;

    private Position secondaryPosition;

    private int jerseyNumber;

    private String photoUrl;

    private LocalDate birthDate;

    private boolean available;
}
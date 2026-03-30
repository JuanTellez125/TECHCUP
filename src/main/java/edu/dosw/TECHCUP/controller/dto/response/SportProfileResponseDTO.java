package edu.dosw.TECHCUP.controller.dto.response;

import edu.dosw.TECHCUP.core.model.enums.Position;
import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SportProfileResponseDTO {

    private Long id;

    private Long userId;

    private String firstName;

    private String lastName;

    private Position primaryPosition;

    private Position secondaryPosition;

    private int jerseyNumber;

    private String photoUrl;

    private LocalDate birthDate;

    private boolean available;
}
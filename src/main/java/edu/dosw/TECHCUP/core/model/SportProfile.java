package edu.dosw.TECHCUP.core.model;

import edu.dosw.TECHCUP.core.model.enums.Position;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SportProfile {
    private Long sportProfileId;
    private User user;
    private Position primaryPosition;
    private Position secondaryPosition;
    private int jerseyNumber;
    private String photoUrl;
    private LocalDate birthDate;
    private boolean available;
    private Integer semester;
}

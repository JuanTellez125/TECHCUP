package edu.dosw.TECHCUP.core.model;

import edu.dosw.TECHCUP.core.model.enums.Position;

import java.time.LocalDate;

public class SportProfile {
    private Long sportProfile_id;
    private User user;
    private Position primaryPosition;
    private Position secondaryPosition;
    private int jerseyNumber;
    private String photoUrl;
    private LocalDate birthDate;
    private boolean available;
}

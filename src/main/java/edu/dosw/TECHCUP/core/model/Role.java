package edu.dosw.TECHCUP.core.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    PLAYER,
    CAPTAIN,
    ORGANIZER,
    REFEREE,
    ADMINISTRATOR;
}

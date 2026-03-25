package edu.dosw.TECHCUP.core.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum TournamentStatus {
    SKETCH,
    ACTIVE,
    INPROGRESS,
    FINALIZED
}

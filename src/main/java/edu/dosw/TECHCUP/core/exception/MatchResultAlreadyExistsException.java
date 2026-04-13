package edu.dosw.TECHCUP.core.exception;

public class MatchResultAlreadyExistsException extends RuntimeException {
    public MatchResultAlreadyExistsException(Long matchId) {
        super("There is already a recorded result for this match");
    }
}
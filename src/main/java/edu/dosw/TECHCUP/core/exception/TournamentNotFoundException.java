package edu.dosw.TECHCUP.core.exception;

public class TournamentNotFoundException extends RuntimeException {
    public TournamentNotFoundException(Long message) {
        super(String.valueOf(message));
    }
}

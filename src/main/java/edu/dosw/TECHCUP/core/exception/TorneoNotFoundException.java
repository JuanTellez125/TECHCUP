package edu.dosw.TECHCUP.core.exception;

public class TorneoNotFoundException extends RuntimeException {
    public TorneoNotFoundException(Long message) {
        super(String.valueOf(message));
    }
}

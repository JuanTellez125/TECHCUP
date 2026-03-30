package edu.dosw.TECHCUP.core.exception;

public class SportProfileException extends RuntimeException {
    public SportProfileException(Long id) {
        super("The sport profile with id " + id + " does not exist");
    }
}

package edu.dosw.TECHCUP.core.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long message) {
        super(String.valueOf(message));
    }
}

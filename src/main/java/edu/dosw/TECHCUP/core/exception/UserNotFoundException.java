package edu.dosw.TECHCUP.core.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super("Usuario con id " + message + " no encontrado.");
    }
}

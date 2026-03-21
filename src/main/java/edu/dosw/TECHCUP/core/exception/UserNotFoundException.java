package edu.dosw.TECHCUP.core.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long message) {
        super("Usuario con id " + message + " no encontrado.");
    }
}

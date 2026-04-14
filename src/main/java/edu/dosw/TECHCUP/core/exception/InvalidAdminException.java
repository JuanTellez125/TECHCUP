package edu.dosw.TECHCUP.core.exception;

public class InvalidAdminException extends RuntimeException {
    public InvalidAdminException(Long userId) {
        super("User with id " + userId + " does not have ADMINISTRATOR role.");
    }
}

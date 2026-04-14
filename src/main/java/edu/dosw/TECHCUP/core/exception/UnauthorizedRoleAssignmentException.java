package edu.dosw.TECHCUP.core.exception;

public class UnauthorizedRoleAssignmentException extends RuntimeException {
    public UnauthorizedRoleAssignmentException() {
        super("Assigning the ADMINISTRATOR role is not allowed through this endpoint.");
    }
}

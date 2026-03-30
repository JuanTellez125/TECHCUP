package edu.dosw.TECHCUP.core.exception;

public class TeamNotFoundException extends RuntimeException {
    public TeamNotFoundException(Long id) {
        super("The team with id " + id + " was not found");
    }

    public TeamNotFoundException(Long id,Long matchId) {
      super("The team with id " + id + " was not found in the match with id " + matchId);
    }
}

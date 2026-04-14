package edu.dosw.TECHCUP.core.exception;

public class TournamentNotFinalizedException extends RuntimeException {
    public TournamentNotFinalizedException(Long tournamentId) {super("El historial del torneo solo se puede ver en torneos finalizados. El torneo con el id " + tournamentId + " No ha finalizado");}
}
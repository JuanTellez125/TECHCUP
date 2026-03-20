package edu.dosw.TECHCUP.core.model;

public class TournamentFactory {
    private TournamentBuilder builder;

    public void crearTorneoBuilder(TournamentBuilder builder) {
        this.builder = builder;
    }
}

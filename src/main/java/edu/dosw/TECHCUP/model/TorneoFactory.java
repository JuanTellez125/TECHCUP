package edu.dosw.TECHCUP.model;

public class TorneoFactory {
    private TorneoBuilder builder;

    public void crearTorneoBuilder(TorneoBuilder builder) {
        this.builder = builder;
    }
}

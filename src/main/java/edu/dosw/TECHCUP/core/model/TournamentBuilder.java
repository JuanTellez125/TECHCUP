package edu.dosw.TECHCUP.core.model;

import java.time.LocalDate;

public interface TournamentBuilder {
    public void buildFechaInicio(LocalDate fechaInicio);
    public void buildFechaFinal(LocalDate fechaFinal);
    public void buildCantidadEquipos(int cantidadEquipos);
    public void buildCostoInscripcion(double  costoInscripcion);
}

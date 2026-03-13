package edu.dosw.TECHCUP.model;

import java.time.LocalDate;

public interface TorneoBuilder {
    public void buildFechaInicio(LocalDate fechaInicio);
    public void buildFechaFinal(LocalDate fechaFinal);
    public void buildCantidadEquipos(int cantidadEquipos);
    public void buildCostoInscripcion(double  costoInscripcion);
}

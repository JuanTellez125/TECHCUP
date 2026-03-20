package edu.dosw.TECHCUP.core.model;

import java.time.LocalDate;

public class TournamentLightningBuilder implements TournamentBuilder {
    private Torneo torneo;

    public TournamentLightningBuilder(Torneo torneo){
        this.torneo = torneo;
    };
    @Override
    public void buildFechaInicio(LocalDate fechaInicio){
        torneo.setFechaInicio(fechaInicio);
    }
    @Override
    public void buildFechaFinal(LocalDate fechaFinal){
        torneo.setFechaFinal(fechaFinal);
    }

    @Override
    public void buildCantidadEquipos(int cantidadEquipos) {
        torneo.setCantidadEquipos(cantidadEquipos);
    }
    @Override
    public void buildCostoInscripcion(double costoInscripcion) {
        torneo.setCostoInscripcion(costoInscripcion);
    }

    public Torneo getResult(){
        return torneo;
    }


}

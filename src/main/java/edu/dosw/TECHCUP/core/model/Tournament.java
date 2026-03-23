package edu.dosw.TECHCUP.core.model;

import lombok.Builder;
import lombok.Data;


import java.time.LocalDate;

@Data
@Builder
public class Tournament {
    private long id;
    private LocalDate fechaInicio;
    private LocalDate fechaFinal;
    private int cantidadEquipos;
    private double costoInscripcion;
    private TournamentStatus estado;


    public void showInfo(){

    }




}

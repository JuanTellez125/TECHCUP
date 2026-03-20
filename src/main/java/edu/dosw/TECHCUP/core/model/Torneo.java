package edu.dosw.TECHCUP.core.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class Torneo {
    private LocalDate fechaInicio;
    private LocalDate fechaFinal;
    private int cantidadEquipos;
    private double costoInscripcion;
    private TournamentStatus estado;


    public void showInfo(){

    }




}

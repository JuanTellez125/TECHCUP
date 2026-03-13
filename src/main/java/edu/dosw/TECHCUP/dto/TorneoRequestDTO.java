package edu.dosw.TECHCUP.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter

public class TorneoRequestDTO {
    private LocalDate fechaInicio;
    private LocalDate fechaFinal;
    private int cantidadEquipos;
    private double costoInscripcion;

}

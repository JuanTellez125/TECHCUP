package edu.dosw.TECHCUP.controller.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class TournamentResponseDTO {
    private Long id;
    private LocalDate fechaInicio;
    private LocalDate fechaFinal;
    private int cantidadEquipos;
    private double costoInscripcion;
    private TournamentStatus estado;
}

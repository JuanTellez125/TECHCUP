package edu.dosw.TECHCUP.controller.dto;

import edu.dosw.TECHCUP.model.EstadoTorneo;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class TorneoResponseDTO {
    private Long id;
    private LocalDate fechaInicio;
    private LocalDate fechaFinal;
    private int cantidadEquipos;
    private double costoInscripcion;
    private EstadoTorneo estado;
}

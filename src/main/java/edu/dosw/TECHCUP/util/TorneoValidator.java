package edu.dosw.TECHCUP.util;

import edu.dosw.TECHCUP.dto.TorneoRequestDTO;
import edu.dosw.TECHCUP.exception.TorneoValidationException;

import java.time.LocalDate;

public class TorneoValidator {

    public static void validar(TorneoRequestDTO dto) {
        validarFechas(dto.getFechaInicio(), dto.getFechaFinal());
        validarCantidadEquipos(dto.getCantidadEquipos());
        validarCostoInscripcion(dto.getCostoInscripcion());

    }


    private static void validarFechas(LocalDate fechaInicio, LocalDate fechaFinal) {
        if (fechaInicio == null) {
            throw new TorneoValidationException("La fecha de inicio no puede ser nula.");
        }
        if (fechaFinal == null) {
            throw new TorneoValidationException("La fecha final no puede ser nula.");
        }
        if (fechaInicio.isBefore(LocalDate.now())) {
            throw new TorneoValidationException("La fecha de inicio no puede ser en el pasado.");
        }
        if (fechaFinal.isBefore(fechaInicio)) {
            throw new TorneoValidationException("La fecha final no puede ser anterior a la fecha de inicio.");
        }
    }

    private static void validarCantidadEquipos(int cantidadEquipos) {
        if (cantidadEquipos <= 0) {
            throw new TorneoValidationException("La cantidad de equipos debe ser mayor a 0.");
        }

    }

    private static void validarCostoInscripcion(double costo) {
        if (costo < 0) {
            throw new TorneoValidationException("El costo de inscripción no puede ser negativo.");
        }
    }
}

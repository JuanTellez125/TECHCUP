package edu.dosw.TECHCUP.util;

import edu.dosw.TECHCUP.controller.dto.TorneoRequestDTO;
import edu.dosw.TECHCUP.exception.TorneoValidationException;
import edu.dosw.TECHCUP.model.EstadoTorneo;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
@Component
public class TorneoValidator {

    public void validar(TorneoRequestDTO dto) {
        validarFechas(dto.getFechaInicio(), dto.getFechaFinal());
        validarCantidadEquipos(dto.getCantidadEquipos());
        validarCostoInscripcion(dto.getCostoInscripcion());

    }


    private void validarFechas(LocalDate fechaInicio, LocalDate fechaFinal) {
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

    private void validarCantidadEquipos(int cantidadEquipos) {
        if (cantidadEquipos <= 0) {
            throw new TorneoValidationException("La cantidad de equipos debe ser mayor a 0.");
        }

    }

    private void validarCostoInscripcion(double costo) {
        if (costo < 0) {
            throw new TorneoValidationException("El costo de inscripción no puede ser negativo.");
        }
    }

    public void validarTransicionEstado(EstadoTorneo estadoActual, EstadoTorneo estadoNuevo) {
        boolean transicionValida = switch (estadoActual) {
            case BORRADOR -> estadoNuevo == EstadoTorneo.ACTIVO;
            case ACTIVO -> estadoNuevo == EstadoTorneo.PROGRESO;
            case PROGRESO -> estadoNuevo == EstadoTorneo.FINALIZADO;
            case FINALIZADO -> false;
        };

        if (!transicionValida) {
            throw new TorneoValidationException(
                    "No se puede cambiar de " + estadoActual + " a " + estadoNuevo +
                            ". El flujo correcto es: BORRADOR → ACTIVO → PROGRESO → FINALIZADO"
            );
        }
    }
}

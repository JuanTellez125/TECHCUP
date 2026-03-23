package edu.dosw.TECHCUP.core.validator;

import edu.dosw.TECHCUP.controller.dto.TournamentRequestDTO;
import edu.dosw.TECHCUP.core.exception.TournamentValidationException;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
@Component
public class TournamentValidator {

    public void validar(TournamentRequestDTO dto) {
        validarFechas(dto.getFechaInicio(), dto.getFechaFinal());
        validarCantidadEquipos(dto.getCantidadEquipos());
        validarCostoInscripcion(dto.getCostoInscripcion());

    }


    private void validarFechas(LocalDate fechaInicio, LocalDate fechaFinal) {
        if (fechaInicio == null) {
            throw new TournamentValidationException("La fecha de inicio no puede ser nula.");
        }
        if (fechaFinal == null) {
            throw new TournamentValidationException("La fecha final no puede ser nula.");
        }
        if (fechaInicio.isBefore(LocalDate.now())) {
            throw new TournamentValidationException("La fecha de inicio no puede ser en el pasado.");
        }
        if (fechaFinal.isBefore(fechaInicio)) {
            throw new TournamentValidationException("La fecha final no puede ser anterior a la fecha de inicio.");
        }
    }

    private void validarCantidadEquipos(int cantidadEquipos) {
        if (cantidadEquipos <= 0) {
            throw new TournamentValidationException("La cantidad de equipos debe ser mayor a 0.");
        }

    }

    private void validarCostoInscripcion(double costo) {
        if (costo < 0) {
            throw new TournamentValidationException("El costo de inscripción no puede ser negativo.");
        }
    }

    public void validarTransicionEstado(TournamentStatus estadoActual, TournamentStatus estadoNuevo) {
        boolean transicionValida = switch (estadoActual) {
            case BORRADOR -> estadoNuevo == TournamentStatus.ACTIVO;
            case ACTIVO -> estadoNuevo == TournamentStatus.PROGRESO;
            case PROGRESO -> estadoNuevo == TournamentStatus.FINALIZADO;
            case FINALIZADO -> false;
        };

        if (!transicionValida) {
            throw new TournamentValidationException(
                    "No se puede cambiar de " + estadoActual + " a " + estadoNuevo +
                            ". El flujo correcto es: BORRADOR → ACTIVO → PROGRESO → FINALIZADO"
            );
        }
    }
}

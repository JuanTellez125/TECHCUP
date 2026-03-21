package edu.dosw.TECHCUP.core.validator;

import edu.dosw.TECHCUP.controller.dto.TournamentRequestDTO;
import edu.dosw.TECHCUP.core.exception.TournamentValidationException;
import edu.dosw.TECHCUP.core.model.TournamentStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
@Slf4j
@Component
public class TournamentValidator {

    public void validar(TournamentRequestDTO dto) {
        log.debug("Starting validation TournamentRequestDTO");

        validarFechas(dto.getFechaInicio(), dto.getFechaFinal());
        validarCantidadEquipos(dto.getCantidadEquipos());
        validarCostoInscripcion(dto.getCostoInscripcion());

        log.debug("TournamentRequestDTO validation successfully completed");

    }


    private void validarFechas(LocalDate fechaInicio, LocalDate fechaFinal) {
        if (fechaInicio == null) {
            log.warn("Validation failed: startdate is null");
            throw new TournamentValidationException("La fecha de inicio no puede ser nula.");
        }
        if (fechaFinal == null) {
            log.warn("Validation failed: finalDate is null");
            throw new TournamentValidationException("La fecha final no puede ser nula.");
        }
        if (fechaInicio.isBefore(LocalDate.now())) {
            log.warn("Validation failed: initialDate {} is the past", fechaInicio);
            throw new TournamentValidationException("La fecha de inicio no puede ser en el pasado.");
        }
        if (fechaFinal.isBefore(fechaInicio)) {
            log.warn("Validation failed: endDate {} is earlier than startDate {}", fechaFinal, fechaInicio);
            throw new TournamentValidationException("La fecha final no puede ser anterior a la fecha de inicio.");
        }
    }

    private void validarCantidadEquipos(int cantidadEquipos) {
        if (cantidadEquipos <= 0) {
            log.warn("Validation failed: quantityEquipment= {} must be greater than 0", cantidadEquipos);
            throw new TournamentValidationException("La cantidad de equipos debe ser mayor a 0.");
        }

    }

    private void validarCostoInscripcion(double costo) {
        if (costo < 0) {
            log.warn("Validation failed: costoInscripcion= {} cannot be negative", costo);
            throw new TournamentValidationException("El costo de inscripción no puede ser negativo.");
        }
    }

    public void validarTransicionEstado(TournamentStatus estadoActual, TournamentStatus estadoNuevo) {
        log.debug("Validating state transition: {} → {}", estadoActual, estadoNuevo);
        boolean transicionValida = switch (estadoActual) {
            case BORRADOR -> estadoNuevo == TournamentStatus.ACTIVO;
            case ACTIVO -> estadoNuevo == TournamentStatus.PROGRESO;
            case PROGRESO -> estadoNuevo == TournamentStatus.FINALIZADO;
            case FINALIZADO -> false;
        };

        if (!transicionValida) {
            log.warn("Invalid state transition: {} → {}", estadoActual, estadoNuevo);
            throw new TournamentValidationException(
                    "No se puede cambiar de " + estadoActual + " a " + estadoNuevo +
                            ". El flujo correcto es: BORRADOR → ACTIVO → PROGRESO → FINALIZADO"
            );
        }
        log.debug("State transition {} → {} is valid", estadoActual, estadoNuevo);
    }
}

package edu.dosw.TECHCUP.core.validator;

import edu.dosw.TECHCUP.controller.dto.request.TournamentRequestDTO;
import edu.dosw.TECHCUP.core.exception.TournamentValidationException;
import edu.dosw.TECHCUP.core.model.enums.TournamentStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
@Component
public class TournamentValidator {

    public void validate(TournamentRequestDTO dto) {
        validateDates(dto.getStartDate(), dto.getEndDate());
        validateTotalTeams(dto.getTotalTeams());
        validateRegistrationCost(dto.getRegistrationCost());
    }

    private void validateDates(LocalDate startDate, LocalDate endDate) {
        if (startDate == null) {
            throw new TournamentValidationException("La fecha de inicio no puede ser nula.");
        }
        if (endDate == null) {
            throw new TournamentValidationException("La fecha final no puede ser nula.");
        }
        if (startDate.isBefore(LocalDate.now())) {
            throw new TournamentValidationException("La fecha de inicio no puede ser en el pasado.");
        }
        if (endDate.isBefore(startDate)) {
            throw new TournamentValidationException("La fecha final no puede ser anterior a la fecha de inicio.");
        }
    }

    private void validateTotalTeams(int totalTeams) {
        if (totalTeams <= 0) {
            throw new TournamentValidationException("La cantidad de equipos debe ser mayor a 0.");
        }
    }

    private void validateRegistrationCost(double cost) {
        if (cost < 0) {
            throw new TournamentValidationException("El costo de inscripción no puede ser negativo.");
        }
    }

    public void validateStatusTransition(TournamentStatus currentStatus, TournamentStatus newStatus) {
        boolean validTransition = switch (currentStatus) {
            case SKETCH     -> newStatus == TournamentStatus.ACTIVE;
            case ACTIVE     -> newStatus == TournamentStatus.INPROGRESS;
            case INPROGRESS -> newStatus == TournamentStatus.FINALIZED;
            case FINALIZED  -> false;
        };

        if (!validTransition) {
            throw new TournamentValidationException(
                    "No se puede cambiar de " + currentStatus + " a " + newStatus +
                            ". El flujo correcto es: SKETCH → ACTIVE → INPROGRESS → FINALIZED"
            );
        }
    }
}

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
            throw new TournamentValidationException("The start date cannot be blank.");
        }
        if (endDate == null) {
            throw new TournamentValidationException("The final date cannot be blank");
        }
        if (startDate.isBefore(LocalDate.now())) {
            throw new TournamentValidationException("The start date cannot be in the past.");
        }
        if (endDate.isBefore(startDate)) {
            throw new TournamentValidationException("La fecha final no puede ser anterior a la fecha de inicio.");
        }
    }

    private void validateTotalTeams(int totalTeams) {
        if (totalTeams <= 0) {
            throw new TournamentValidationException("The number of teams must be greater than 0.");
        }
    }

    private void validateRegistrationCost(double cost) {
        if (cost < 0) {
            throw new TournamentValidationException("The cost of registration cannot be negative.");
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
                    "You can't change " + currentStatus + " to " + newStatus +
                            ". The correct flow is: SKETCH → ACTIVE → INPROGRESS → FINALIZED"
            );
        }
    }
}

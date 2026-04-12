package edu.dosw.TECHCUP.core.validator;

import edu.dosw.TECHCUP.controller.dto.request.TournamentRequestDTO;
import edu.dosw.TECHCUP.core.exception.TournamentValidationException;
import edu.dosw.TECHCUP.core.model.enums.TournamentStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;

class TournamentValidatorTest {

    private TournamentValidator validator;

    @BeforeEach
    void setUp() {
        validator = new TournamentValidator();
    }

    // ─── validate() ───────────────────────────────────────────────────────────

    @Test
    void validate_validDTO_doesNotThrow() {
        TournamentRequestDTO dto = validDTO();
        assertThatCode(() -> validator.validate(dto)).doesNotThrowAnyException();
    }

    @Test
    void validate_nullStartDate_throws() {
        TournamentRequestDTO dto = validDTO();
        dto.setStartDate(null);
        assertThatThrownBy(() -> validator.validate(dto))
                .isInstanceOf(TournamentValidationException.class)
                .hasMessageContaining("start date");
    }

    @Test
    void validate_nullEndDate_throws() {
        TournamentRequestDTO dto = validDTO();
        dto.setEndDate(null);
        assertThatThrownBy(() -> validator.validate(dto))
                .isInstanceOf(TournamentValidationException.class)
                .hasMessageContaining("final date");
    }

    @Test
    void validate_startDateInPast_throws() {
        TournamentRequestDTO dto = validDTO();
        dto.setStartDate(LocalDate.now().minusDays(1));
        assertThatThrownBy(() -> validator.validate(dto))
                .isInstanceOf(TournamentValidationException.class)
                .hasMessageContaining("past");
    }

    @Test
    void validate_endDateBeforeStartDate_throws() {
        TournamentRequestDTO dto = validDTO();
        dto.setStartDate(LocalDate.now().plusDays(5));
        dto.setEndDate(LocalDate.now().plusDays(2));
        assertThatThrownBy(() -> validator.validate(dto))
                .isInstanceOf(TournamentValidationException.class);
    }

    @Test
    void validate_zeroTotalTeams_throws() {
        TournamentRequestDTO dto = validDTO();
        dto.setTotalTeams(0);
        assertThatThrownBy(() -> validator.validate(dto))
                .isInstanceOf(TournamentValidationException.class)
                .hasMessageContaining("greater than 0");
    }

    @Test
    void validate_negativeCost_throws() {
        TournamentRequestDTO dto = validDTO();
        dto.setRegistrationCost(-1.0);
        assertThatThrownBy(() -> validator.validate(dto))
                .isInstanceOf(TournamentValidationException.class)
                .hasMessageContaining("negative");
    }

    @Test
    void validate_zeroCost_doesNotThrow() {
        TournamentRequestDTO dto = validDTO();
        dto.setRegistrationCost(0.0);
        assertThatCode(() -> validator.validate(dto)).doesNotThrowAnyException();
    }

    // ─── validateStatusTransition ─────────────────────────────────────────────

    @Test
    void statusTransition_sketchToActive_valid() {
        assertThatCode(() -> validator.validateStatusTransition(TournamentStatus.SKETCH, TournamentStatus.ACTIVE))
                .doesNotThrowAnyException();
    }

    @Test
    void statusTransition_activeToInprogress_valid() {
        assertThatCode(() -> validator.validateStatusTransition(TournamentStatus.ACTIVE, TournamentStatus.INPROGRESS))
                .doesNotThrowAnyException();
    }

    @Test
    void statusTransition_inprogressToFinalized_valid() {
        assertThatCode(() -> validator.validateStatusTransition(TournamentStatus.INPROGRESS, TournamentStatus.FINALIZED))
                .doesNotThrowAnyException();
    }

    @Test
    void statusTransition_sketchToFinalized_invalid() {
        assertThatThrownBy(() -> validator.validateStatusTransition(TournamentStatus.SKETCH, TournamentStatus.FINALIZED))
                .isInstanceOf(TournamentValidationException.class)
                .hasMessageContaining("SKETCH");
    }

    @Test
    void statusTransition_finalizedToAny_invalid() {
        assertThatThrownBy(() -> validator.validateStatusTransition(TournamentStatus.FINALIZED, TournamentStatus.ACTIVE))
                .isInstanceOf(TournamentValidationException.class)
                .hasMessageContaining("FINALIZED");
    }

    @Test
    void statusTransition_activeToFinalized_invalid() {
        assertThatThrownBy(() -> validator.validateStatusTransition(TournamentStatus.ACTIVE, TournamentStatus.FINALIZED))
                .isInstanceOf(TournamentValidationException.class);
    }

    @Test
    void statusTransition_sketchToInprogress_invalid() {
        assertThatThrownBy(() -> validator.validateStatusTransition(TournamentStatus.SKETCH, TournamentStatus.INPROGRESS))
                .isInstanceOf(TournamentValidationException.class);
    }

    // ─── Helper ───────────────────────────────────────────────────────────────

    private TournamentRequestDTO validDTO() {
        return TournamentRequestDTO.builder()
                .startDate(LocalDate.now().plusDays(1))
                .endDate(LocalDate.now().plusDays(30))
                .totalTeams(8)
                .registrationCost(50.0)
                .build();
    }
}

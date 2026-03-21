package edu.dosw.TECHCUP.validator;

import edu.dosw.TECHCUP.controller.dto.TournamentRequestDTO;
import edu.dosw.TECHCUP.core.validator.TournamentValidator;
import edu.dosw.TECHCUP.core.exception.TournamentValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class TournamentValidatorTest {

    private TournamentRequestDTO dto;
    private TournamentValidator tournamentValidator = new TournamentValidator();
    @BeforeEach
    void setUp() {
        dto = new TournamentRequestDTO();
        dto.setFechaInicio(LocalDate.now().plusDays(1));
        dto.setFechaFinal(LocalDate.now().plusDays(5));
        dto.setCantidadEquipos(8);
        dto.setCostoInscripcion(50000);
    }


    // CP-001: Datos completamente válidos
    @Test
    void validar_conDatosValidos_noDebeLanzarExcepcion() {
        assertDoesNotThrow(() -> tournamentValidator.validar(dto));
    }

    // CC-004: Costo cero es válido (torneo gratuito)
    @Test
    void validar_conCostoCero_noDebeLanzarExcepcion() {
        dto.setCostoInscripcion(0);
        assertDoesNotThrow(() -> tournamentValidator.validar(dto));
    }


    // CE-001: Fecha inicio nula
    @Test
    void validar_conFechaInicioNula_debeLanzarExcepcion() {
        dto.setFechaInicio(null);

        TournamentValidationException ex = assertThrows(
                TournamentValidationException.class,
                () -> tournamentValidator.validar(dto)
        );
        assertEquals("La fecha de inicio no puede ser nula.", ex.getMessage());
    }

    // CE-002: Fecha final nula
    @Test
    void validar_conFechaFinalNula_debeLanzarExcepcion() {
        dto.setFechaFinal(null);

        TournamentValidationException ex = assertThrows(
                TournamentValidationException.class,
                () -> tournamentValidator.validar(dto)
        );
        assertEquals("La fecha final no puede ser nula.", ex.getMessage());
    }

    // CC-001: Fecha inicio en el pasado
    @Test
    void validar_conFechaInicioEnElPasado_debeLanzarExcepcion() {
        dto.setFechaInicio(LocalDate.now().minusDays(1));

        TournamentValidationException ex = assertThrows(
                TournamentValidationException.class,
                () -> tournamentValidator.validar(dto)
        );
        assertEquals("La fecha de inicio no puede ser en el pasado.", ex.getMessage());
    }

    // CC-002: Fecha final anterior a fecha inicio
    @Test
    void validar_conFechaFinalAntesDeFechaInicio_debeLanzarExcepcion() {
        dto.setFechaInicio(LocalDate.now().plusDays(5));
        dto.setFechaFinal(LocalDate.now().plusDays(1));

        TournamentValidationException ex = assertThrows(
                TournamentValidationException.class,
                () -> tournamentValidator.validar(dto)
        );
        assertEquals("La fecha final no puede ser anterior a la fecha de inicio.", ex.getMessage());
    }

    // CE-003: Cantidad de equipos en cero
    @Test
    void validar_conCantidadEquiposCero_debeLanzarExcepcion() {
        dto.setCantidadEquipos(0);

        TournamentValidationException ex = assertThrows(
                TournamentValidationException.class,
                () -> tournamentValidator.validar(dto)
        );
        assertEquals("La cantidad de equipos debe ser mayor a 0.", ex.getMessage());
    }


    // CE-004: Costo de inscripción negativo
    @Test
    void validar_conCostoNegativo_debeLanzarExcepcion() {
        dto.setCostoInscripcion(-1000);

        TournamentValidationException ex = assertThrows(
                TournamentValidationException.class,
                () -> tournamentValidator.validar(dto)
        );
        assertEquals("El costo de inscripción no puede ser negativo.", ex.getMessage());
    }
}

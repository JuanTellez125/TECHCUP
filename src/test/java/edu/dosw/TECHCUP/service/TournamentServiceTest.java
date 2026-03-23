package edu.dosw.TECHCUP.service;

import edu.dosw.TECHCUP.controller.dto.TournamentRequestDTO;
import edu.dosw.TECHCUP.core.exception.TournamentNotFoundException;
import edu.dosw.TECHCUP.core.exception.TournamentValidationException;
import edu.dosw.TECHCUP.core.model.Tournament;
import edu.dosw.TECHCUP.core.service.TournamentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TournamentServiceTest {

    private TournamentService tournamentService;
    private TournamentRequestDTO requestValido;

    @BeforeEach
    void setUp() {
        tournamentService = new TournamentService();

        requestValido = new TournamentRequestDTO();
        requestValido.setFechaInicio(LocalDate.now().plusDays(1));
        requestValido.setFechaFinal(LocalDate.now().plusDays(5));
        requestValido.setCantidadEquipos(8);
        requestValido.setCostoInscripcion(50000);
    }

    @Test
    void crearTorneo_conDatosValidos_debeRetornarTorneoEnBorrador() {
        Tournament response = tournamentService.crearTorneo(requestValido);

        assertNotNull(response);
        assertNotNull(response.getId());
        assertEquals(TournamentStatus.BORRADOR, response.getEstado());
        assertEquals(requestValido.getFechaInicio(), response.getFechaInicio());
        assertEquals(requestValido.getFechaFinal(), response.getFechaFinal());
        assertEquals(requestValido.getCantidadEquipos(), response.getCantidadEquipos());
        assertEquals(requestValido.getCostoInscripcion(), response.getCostoInscripcion());
    }

    @Test
    void listarTorneos_conTorneosCreados_debeRetornarLista() {
        tournamentService.crearTorneo(requestValido);
        tournamentService.crearTorneo(requestValido);

        List<Tournament> torneos = tournamentService.listarTorneos();

        assertEquals(2, torneos.size());
    }

    @Test
    void listarTorneos_sinTorneosCreados_debeRetornarListaVacia() {
        List<Tournament> torneos = tournamentService.listarTorneos();

        assertNotNull(torneos);
        assertTrue(torneos.isEmpty());
    }

    @Test
    void obtenerTorneoPorId_conIdExistente_debeRetornarTorneo() {
        Tournament creado = tournamentService.crearTorneo(requestValido);

        Tournament encontrado = tournamentService.obtenerTorneoPorId(creado.getId());

        assertNotNull(encontrado);
        assertEquals(creado.getId(), encontrado.getId());
    }

    @Test
    void cambiarEstado_deBorradorAActivo_debeActualizarEstado() {
        Tournament creado = tournamentService.crearTorneo(requestValido);

        Tournament actualizado = tournamentService.cambiarEstado(creado.getId(), "ACTIVO");

        assertEquals(TournamentStatus.ACTIVO, actualizado.getEstado());
    }

    @Test
    void cambiarEstado_deActivoAProgreso_debeActualizarEstado() {
        Tournament creado = tournamentService.crearTorneo(requestValido);
        tournamentService.cambiarEstado(creado.getId(), "ACTIVO");

        Tournament actualizado = tournamentService.cambiarEstado(creado.getId(), "PROGRESO");

        assertEquals(TournamentStatus.PROGRESO, actualizado.getEstado());
    }

    @Test
    void cambiarEstado_deProgresoAFinalizado_debeActualizarEstado() {
        Tournament creado = tournamentService.crearTorneo(requestValido);
        tournamentService.cambiarEstado(creado.getId(), "ACTIVO");
        tournamentService.cambiarEstado(creado.getId(), "PROGRESO");

        Tournament actualizado = tournamentService.cambiarEstado(creado.getId(), "FINALIZADO");

        assertEquals(TournamentStatus.FINALIZADO, actualizado.getEstado());
    }

    @Test
    void crearTorneo_sinFechaInicio_debeLanzarExcepcion() {
        requestValido.setFechaInicio(null);

        assertThrows(TournamentValidationException.class,
                () -> tournamentService.crearTorneo(requestValido));
    }

    @Test
    void crearTorneo_sinFechaFinal_debeLanzarExcepcion() {
        requestValido.setFechaFinal(null);

        assertThrows(TournamentValidationException.class,
                () -> tournamentService.crearTorneo(requestValido));
    }

    @Test
    void crearTorneo_conCantidadEquiposCero_debeLanzarExcepcion() {
        requestValido.setCantidadEquipos(0);

        assertThrows(TournamentValidationException.class,
                () -> tournamentService.crearTorneo(requestValido));
    }

    @Test
    void crearTorneo_conCostoNegativo_debeLanzarExcepcion() {
        requestValido.setCostoInscripcion(-1000);

        assertThrows(TournamentValidationException.class,
                () -> tournamentService.crearTorneo(requestValido));
    }

    @Test
    void obtenerTorneoPorId_conIdInexistente_debeLanzarExcepcion() {
        assertThrows(TournamentNotFoundException.class,
                () -> tournamentService.obtenerTorneoPorId(99L));
    }

    @Test
    void cambiarEstado_conEstadoInvalido_debeLanzarExcepcion() {
        Tournament creado = tournamentService.crearTorneo(requestValido);

        TournamentValidationException ex = assertThrows(TournamentValidationException.class,
                () -> tournamentService.cambiarEstado(creado.getId(), "INVENTADO"));

        assertTrue(ex.getMessage().contains("Estado inválido"));
    }

    @Test
    void crearTorneo_conFechaInicioEnElPasado_debeLanzarExcepcion() {
        requestValido.setFechaInicio(LocalDate.now().minusDays(1));

        assertThrows(TournamentValidationException.class,
                () -> tournamentService.crearTorneo(requestValido));
    }

    @Test
    void crearTorneo_conFechaFinalAntesDeInicio_debeLanzarExcepcion() {
        requestValido.setFechaInicio(LocalDate.now().plusDays(5));
        requestValido.setFechaFinal(LocalDate.now().plusDays(1));

        assertThrows(TournamentValidationException.class,
                () -> tournamentService.crearTorneo(requestValido));
    }

    @Test
    void crearTorneo_conCostoCero_debeCrearseSinError() {
        requestValido.setCostoInscripcion(0);

        Tournament response = tournamentService.crearTorneo(requestValido);

        assertNotNull(response);
        assertEquals(0, response.getCostoInscripcion());
    }

    @Test
    void cambiarEstado_deBorradorAFinalizado_debeLanzarExcepcion() {
        Tournament creado = tournamentService.crearTorneo(requestValido);

        assertThrows(TournamentValidationException.class,
                () -> tournamentService.cambiarEstado(creado.getId(), "FINALIZADO"));
    }
}
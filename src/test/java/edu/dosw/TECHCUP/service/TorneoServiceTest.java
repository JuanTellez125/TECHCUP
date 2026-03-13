package edu.dosw.TECHCUP.service;

import edu.dosw.TECHCUP.controller.dto.TorneoRequestDTO;
import edu.dosw.TECHCUP.controller.dto.TorneoResponseDTO;
import edu.dosw.TECHCUP.exception.TorneoNotFoundException;
import edu.dosw.TECHCUP.exception.TorneoValidationException;
import edu.dosw.TECHCUP.model.EstadoTorneo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TorneoServiceTest {

    private TorneoService torneoService;
    private TorneoRequestDTO requestValido;

    // Se ejecuta antes de CADA prueba
    @BeforeEach
    void setUp() {
        torneoService = new TorneoService();

        // DTO válido base para reutilizar en los tests
        requestValido = new TorneoRequestDTO();
        requestValido.setFechaInicio(LocalDate.now().plusDays(1));
        requestValido.setFechaFinal(LocalDate.now().plusDays(5));
        requestValido.setCantidadEquipos(8);
        requestValido.setCostoInscripcion(50000);
    }

    // =============================================
    // ✅ CASOS DE ÉXITO
    // =============================================

    // CP-001: Crear torneo con datos válidos
    @Test
    void crearTorneo_conDatosValidos_debeRetornarTorneoEnBorrador() {
        TorneoResponseDTO response = torneoService.crearTorneo(requestValido);

        assertNotNull(response);
        assertNotNull(response.getId());
        assertEquals(EstadoTorneo.BORRADOR, response.getEstado());
        assertEquals(requestValido.getFechaInicio(), response.getFechaInicio());
        assertEquals(requestValido.getFechaFinal(), response.getFechaFinal());
        assertEquals(requestValido.getCantidadEquipos(), response.getCantidadEquipos());
        assertEquals(requestValido.getCostoInscripcion(), response.getCostoInscripcion());
    }

    // CP-002: Listar todos los torneos
    @Test
    void listarTorneos_conTorneosCreados_debeRetornarLista() {
        torneoService.crearTorneo(requestValido);
        torneoService.crearTorneo(requestValido);

        List<TorneoResponseDTO> torneos = torneoService.listarTorneos();

        assertEquals(2, torneos.size());
    }

    // CC-006: Listar torneos cuando no hay ninguno
    @Test
    void listarTorneos_sinTorneosCreados_debeRetornarListaVacia() {
        List<TorneoResponseDTO> torneos = torneoService.listarTorneos();

        assertNotNull(torneos);
        assertTrue(torneos.isEmpty());
    }

    // CP-003: Obtener torneo por id existente
    @Test
    void obtenerTorneoPorId_conIdExistente_debeRetornarTorneo() {
        TorneoResponseDTO creado = torneoService.crearTorneo(requestValido);

        TorneoResponseDTO encontrado = torneoService.obtenerTorneoPorId(creado.getId());

        assertNotNull(encontrado);
        assertEquals(creado.getId(), encontrado.getId());
    }

    // CP-004: Cambiar estado de BORRADOR a ACTIVO
    @Test
    void cambiarEstado_deBorradorAActivo_debeActualizarEstado() {
        TorneoResponseDTO creado = torneoService.crearTorneo(requestValido);

        TorneoResponseDTO actualizado = torneoService.cambiarEstado(creado.getId(), "ACTIVO");

        assertEquals(EstadoTorneo.ACTIVO, actualizado.getEstado());
    }

    // CP-005: Cambiar estado de ACTIVO a PROGRESO
    @Test
    void cambiarEstado_deActivoAProgreso_debeActualizarEstado() {
        TorneoResponseDTO creado = torneoService.crearTorneo(requestValido);
        torneoService.cambiarEstado(creado.getId(), "ACTIVO");

        TorneoResponseDTO actualizado = torneoService.cambiarEstado(creado.getId(), "PROGRESO");

        assertEquals(EstadoTorneo.PROGRESO, actualizado.getEstado());
    }

    // CP-006: Cambiar estado de PROGRESO a FINALIZADO
    @Test
    void cambiarEstado_deProgresoAFinalizado_debeActualizarEstado() {
        TorneoResponseDTO creado = torneoService.crearTorneo(requestValido);
        torneoService.cambiarEstado(creado.getId(), "ACTIVO");
        torneoService.cambiarEstado(creado.getId(), "PROGRESO");

        TorneoResponseDTO actualizado = torneoService.cambiarEstado(creado.getId(), "FINALIZADO");

        assertEquals(EstadoTorneo.FINALIZADO, actualizado.getEstado());
    }


    // CE-001: Crear torneo sin fecha inicio
    @Test
    void crearTorneo_sinFechaInicio_debeLanzarExcepcion() {
        requestValido.setFechaInicio(null);

        assertThrows(TorneoValidationException.class,
                () -> torneoService.crearTorneo(requestValido));
    }

    // CE-002: Crear torneo sin fecha final
    @Test
    void crearTorneo_sinFechaFinal_debeLanzarExcepcion() {
        requestValido.setFechaFinal(null);

        assertThrows(TorneoValidationException.class,
                () -> torneoService.crearTorneo(requestValido));
    }

    // CE-003: Crear torneo con cantidad de equipos cero
    @Test
    void crearTorneo_conCantidadEquiposCero_debeLanzarExcepcion() {
        requestValido.setCantidadEquipos(0);

        assertThrows(TorneoValidationException.class,
                () -> torneoService.crearTorneo(requestValido));
    }

    // CE-004: Crear torneo con costo negativo
    @Test
    void crearTorneo_conCostoNegativo_debeLanzarExcepcion() {
        requestValido.setCostoInscripcion(-1000);

        assertThrows(TorneoValidationException.class,
                () -> torneoService.crearTorneo(requestValido));
    }

    // CE-005: Obtener torneo con id inexistente
    @Test
    void obtenerTorneoPorId_conIdInexistente_debeLanzarExcepcion() {
        assertThrows(TorneoNotFoundException.class,
                () -> torneoService.obtenerTorneoPorId(99L));
    }

    // CE-006: Cambiar estado con valor inválido
    @Test
    void cambiarEstado_conEstadoInvalido_debeLanzarExcepcion() {
        TorneoResponseDTO creado = torneoService.crearTorneo(requestValido);

        TorneoValidationException ex = assertThrows(TorneoValidationException.class,
                () -> torneoService.cambiarEstado(creado.getId(), "INVENTADO"));

        assertTrue(ex.getMessage().contains("Estado inválido"));
    }

    // =============================================
    // ⚠️ CASOS CONDICIONALES
    // =============================================

    // CC-001: Fecha inicio en el pasado
    @Test
    void crearTorneo_conFechaInicioEnElPasado_debeLanzarExcepcion() {
        requestValido.setFechaInicio(LocalDate.now().minusDays(1));

        assertThrows(TorneoValidationException.class,
                () -> torneoService.crearTorneo(requestValido));
    }

    // CC-002: Fecha final antes que fecha inicio
    @Test
    void crearTorneo_conFechaFinalAntesDeInicio_debeLanzarExcepcion() {
        requestValido.setFechaInicio(LocalDate.now().plusDays(5));
        requestValido.setFechaFinal(LocalDate.now().plusDays(1));

        assertThrows(TorneoValidationException.class,
                () -> torneoService.crearTorneo(requestValido));
    }


    // CC-004: Costo cero es válido (torneo gratuito)
    @Test
    void crearTorneo_conCostoCero_debeCrearseSinError() {
        requestValido.setCostoInscripcion(0);

        TorneoResponseDTO response = torneoService.crearTorneo(requestValido);

        assertNotNull(response);
        assertEquals(0, response.getCostoInscripcion());
    }

    // CC-005: No se puede saltar estados (BORRADOR → FINALIZADO)
    @Test
    void cambiarEstado_deBorradorAFinalizado_debeLanzarExcepcion() {
        TorneoResponseDTO creado = torneoService.crearTorneo(requestValido);

        assertThrows(TorneoValidationException.class,
                () -> torneoService.cambiarEstado(creado.getId(), "FINALIZADO"));
    }
}

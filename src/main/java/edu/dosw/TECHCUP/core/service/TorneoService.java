package edu.dosw.TECHCUP.core.service;

import edu.dosw.TECHCUP.controller.dto.TorneoRequestDTO;
import edu.dosw.TECHCUP.controller.dto.TorneoResponseDTO;
import edu.dosw.TECHCUP.core.exception.TorneoNotFoundException;
import edu.dosw.TECHCUP.core.exception.TorneoValidationException;
import edu.dosw.TECHCUP.core.model.EstadoTorneo;
import edu.dosw.TECHCUP.core.model.Torneo;
import edu.dosw.TECHCUP.core.model.TorneoFactory;
import edu.dosw.TECHCUP.core.model.TorneoRelampagoBuilder;
import edu.dosw.TECHCUP.controller.mapper.TorneoMapper;
import edu.dosw.TECHCUP.core.validator.TorneoValidator;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TorneoService {

    // Simulamos una base de datos en memoria (luego se reemplaza con un Repository)
    private final Map<Long, Torneo> torneos = new HashMap<>();
    private Long contadorId = 1L;
    private TorneoValidator torneoValidator = new TorneoValidator();
    private final TorneoFactory torneoFactory;

    public TorneoService() {
        this.torneoFactory = new TorneoFactory();
    }

    public TorneoResponseDTO crearTorneo(TorneoRequestDTO request) {
        // 1. Validar los datos de entrada
        torneoValidator.validar(request);

        // 2. Usar el Builder para construir el Torneo
        Torneo torneo = new Torneo();
        TorneoRelampagoBuilder builder = new TorneoRelampagoBuilder(torneo);
        torneoFactory.crearTorneoBuilder(builder);
        builder.buildFechaInicio(request.getFechaInicio());
        builder.buildFechaFinal(request.getFechaFinal());
        builder.buildCantidadEquipos(request.getCantidadEquipos());
        builder.buildCostoInscripcion(request.getCostoInscripcion());

        // 3. Obtener el torneo construido y asignar estado inicial
        Torneo torneoCreado = builder.getResult();
        torneoCreado.setEstado(EstadoTorneo.BORRADOR);

        // 4. Guardar en "base de datos"
        torneos.put(contadorId, torneoCreado);

        // 5. Mapear a DTO de respuesta
        TorneoResponseDTO response = TorneoMapper.toResponseDTO(torneoCreado);
        response.setId(contadorId);
        contadorId++;

        return response;
    }

    public List<TorneoResponseDTO> listarTorneos() {
        return torneos.entrySet().stream()
                .map(entry -> {
                    TorneoResponseDTO dto = TorneoMapper.toResponseDTO(entry.getValue());
                    dto.setId(entry.getKey());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public TorneoResponseDTO obtenerTorneoPorId(Long id) {
        Torneo torneo = torneos.get(id);
        if (torneo == null) {
            throw new TorneoNotFoundException(id);
        }
        TorneoResponseDTO dto = TorneoMapper.toResponseDTO(torneo);
        dto.setId(id);
        return dto;
    }

    public TorneoResponseDTO cambiarEstado(Long id, String nuevoEstado) {
        Torneo torneo = torneos.get(id);
        if (torneo == null) {
            throw new TorneoNotFoundException(id);
        }

        // Validar que el estado recibido sea válido
        EstadoTorneo estadoEnum;
        try {
            estadoEnum = EstadoTorneo.valueOf(nuevoEstado.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new TorneoValidationException("Estado inválido: " + nuevoEstado +
                    ". Los estados válidos son: BORRADOR, ACTIVO, PROGRESO, FINALIZADO");
        }

        torneoValidator.validarTransicionEstado(torneo.getEstado(), estadoEnum);
        torneo.setEstado(estadoEnum);

        TorneoResponseDTO dto = TorneoMapper.toResponseDTO(torneo);
        dto.setId(id);
        return dto;

    }
}
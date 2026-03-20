package edu.dosw.TECHCUP.core.service;

import edu.dosw.TECHCUP.controller.dto.TournamentRequestDTO;
import edu.dosw.TECHCUP.controller.dto.TournamentResponseDTO;
import edu.dosw.TECHCUP.core.exception.TournamentNotFoundException;
import edu.dosw.TECHCUP.core.exception.TournamentValidationException;
import edu.dosw.TECHCUP.core.model.TournamentStatus;
import edu.dosw.TECHCUP.core.model.Torneo;
import edu.dosw.TECHCUP.core.model.TournamentFactory;
import edu.dosw.TECHCUP.core.model.TournamentLightningBuilder;
import edu.dosw.TECHCUP.controller.mapper.TournamentMapper;
import edu.dosw.TECHCUP.core.validator.TorneoValidator;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TournamentService {

    // Simulamos una base de datos en memoria (luego se reemplaza con un Repository)
    private final Map<Long, Torneo> torneos = new HashMap<>();
    private Long contadorId = 1L;
    private TorneoValidator torneoValidator = new TorneoValidator();
    private final TournamentFactory tournamentFactory;

    public TournamentService() {
        this.tournamentFactory = new TournamentFactory();
    }

    public TournamentResponseDTO crearTorneo(TournamentRequestDTO request) {
        // 1. Validar los datos de entrada
        torneoValidator.validar(request);

        // 2. Usar el Builder para construir el Torneo
        Torneo torneo = new Torneo();
        TournamentLightningBuilder builder = new TournamentLightningBuilder(torneo);
        tournamentFactory.crearTorneoBuilder(builder);
        builder.buildFechaInicio(request.getFechaInicio());
        builder.buildFechaFinal(request.getFechaFinal());
        builder.buildCantidadEquipos(request.getCantidadEquipos());
        builder.buildCostoInscripcion(request.getCostoInscripcion());

        // 3. Obtener el torneo construido y asignar estado inicial
        Torneo torneoCreado = builder.getResult();
        torneoCreado.setEstado(TournamentStatus.BORRADOR);

        // 4. Guardar en "base de datos"
        torneos.put(contadorId, torneoCreado);

        // 5. Mapear a DTO de respuesta
        TournamentResponseDTO response = TournamentMapper.toResponseDTO(torneoCreado);
        response.setId(contadorId);
        contadorId++;

        return response;
    }

    public List<TournamentResponseDTO> listarTorneos() {
        return torneos.entrySet().stream()
                .map(entry -> {
                    TournamentResponseDTO dto = TournamentMapper.toResponseDTO(entry.getValue());
                    dto.setId(entry.getKey());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public TournamentResponseDTO obtenerTorneoPorId(Long id) {
        Torneo torneo = torneos.get(id);
        if (torneo == null) {
            throw new TournamentNotFoundException(id);
        }
        TournamentResponseDTO dto = TournamentMapper.toResponseDTO(torneo);
        dto.setId(id);
        return dto;
    }

    public TournamentResponseDTO cambiarEstado(Long id, String nuevoEstado) {
        Torneo torneo = torneos.get(id);
        if (torneo == null) {
            throw new TournamentNotFoundException(id);
        }

        // Validar que el estado recibido sea válido
        TournamentStatus estadoEnum;
        try {
            estadoEnum = TournamentStatus.valueOf(nuevoEstado.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new TournamentValidationException("Estado inválido: " + nuevoEstado +
                    ". Los estados válidos son: BORRADOR, ACTIVO, PROGRESO, FINALIZADO");
        }

        torneoValidator.validarTransicionEstado(torneo.getEstado(), estadoEnum);
        torneo.setEstado(estadoEnum);

        TournamentResponseDTO dto = TournamentMapper.toResponseDTO(torneo);
        dto.setId(id);
        return dto;

    }
}
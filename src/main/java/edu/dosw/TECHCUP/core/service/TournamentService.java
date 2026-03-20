package edu.dosw.TECHCUP.core.service;

import edu.dosw.TECHCUP.controller.dto.TournamentRequestDTO;
import edu.dosw.TECHCUP.core.exception.TournamentNotFoundException;
import edu.dosw.TECHCUP.core.exception.TournamentValidationException;
import edu.dosw.TECHCUP.core.model.TournamentStatus;
import edu.dosw.TECHCUP.core.model.Tournament;
import edu.dosw.TECHCUP.core.model.TournamentFactory;
import edu.dosw.TECHCUP.core.model.TournamentLightningBuilder;
import edu.dosw.TECHCUP.core.validator.TournamentValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TournamentService {

    private final Map<Long, Tournament> torneos = new HashMap<>();
    private Long contadorId = 1L;
    private TournamentValidator tournamentValidator = new TournamentValidator();
    private final TournamentFactory tournamentFactory;

    public TournamentService() {
        this.tournamentFactory = new TournamentFactory();
    }

    public Tournament crearTorneo(TournamentRequestDTO request) {

        log.debug("Starting tournament creation - startDate: {}, teams: {}", request.getFechaInicio(), request.getCantidadEquipos());

        tournamentValidator.validar(request);

        log.debug("Validation passed for new tournament");

        Tournament torneo = new Tournament();
        TournamentLightningBuilder builder = new TournamentLightningBuilder(torneo);
        tournamentFactory.crearTorneoBuilder(builder);
        builder.buildFechaInicio(request.getFechaInicio());
        builder.buildFechaFinal(request.getFechaFinal());
        builder.buildCantidadEquipos(request.getCantidadEquipos());
        builder.buildCostoInscripcion(request.getCostoInscripcion());

        Tournament torneoCreado = builder.getResult();
        torneoCreado.setEstado(TournamentStatus.BORRADOR);
        torneoCreado.setId(contadorId);

        torneos.put(contadorId, torneoCreado);
        contadorId++;

        log.info("Tournament created with id: {} and state: {}", torneoCreado.getId(), torneoCreado.getEstado());

        return torneoCreado;
    }

    public List<Tournament> listarTorneos() {

        log.debug("Listando torneos - total en memoria: {}", torneos.size());

        return torneos.values().stream()
                .collect(Collectors.toList());
    }

    public Tournament obtenerTorneoPorId(Long id) {

        log.debug("Looking for tournament with id: {}", id);

        Tournament torneo = torneos.get(id);
        if (torneo == null) {

            log.warn("Tournament with id {} not found in the system", id);

            throw new TournamentNotFoundException(id);
        }
        return torneo;
    }

    public Tournament cambiarEstado(Long id, String nuevoEstado) {

        log.debug("Trying to change the tournament status {} a ''{}", id, nuevoEstado);

        Tournament torneo = torneos.get(id);
        if (torneo == null) {

            log.warn("Tournament with id {} not found when trying to change state", id);

            throw new TournamentNotFoundException(id);
        }

        TournamentStatus estadoEnum;
        try {
            estadoEnum = TournamentStatus.valueOf(nuevoEstado.toUpperCase());
        } catch (IllegalArgumentException e) {

            log.warn("Invalid status received: '{}' for tournament {}", nuevoEstado, id);

            throw new TournamentValidationException("Estado inválido: " + nuevoEstado +
                    ". Los estados válidos son: BORRADOR, ACTIVO, PROGRESO, FINALIZADO");
        }

        tournamentValidator.validarTransicionEstado(torneo.getEstado(), estadoEnum);
        torneo.setEstado(estadoEnum);

        log.info("Estado del torneo {} cambiado de {} a {}", id, torneo.getEstado(), estadoEnum);

        return torneo;
    }
}
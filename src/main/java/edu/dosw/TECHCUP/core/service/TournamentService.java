package edu.dosw.TECHCUP.core.service;

import edu.dosw.TECHCUP.controller.dto.TournamentRequestDTO;
import edu.dosw.TECHCUP.core.exception.TournamentNotFoundException;
import edu.dosw.TECHCUP.core.exception.TournamentValidationException;
import edu.dosw.TECHCUP.core.model.Tournament;
import edu.dosw.TECHCUP.core.validator.TournamentValidator;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        tournamentValidator.validar(request);

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

        return torneoCreado;
    }

    public List<Tournament> listarTorneos() {
        return torneos.values().stream()
                .collect(Collectors.toList());
    }

    public Tournament obtenerTorneoPorId(Long id) {
        Tournament torneo = torneos.get(id);
        if (torneo == null) {
            throw new TournamentNotFoundException(id);
        }
        return torneo;
    }

    public Tournament cambiarEstado(Long id, String nuevoEstado) {
        Tournament torneo = torneos.get(id);
        if (torneo == null) {
            throw new TournamentNotFoundException(id);
        }

        TournamentStatus estadoEnum;
        try {
            estadoEnum = TournamentStatus.valueOf(nuevoEstado.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new TournamentValidationException("Estado inválido: " + nuevoEstado +
                    ". Los estados válidos son: BORRADOR, ACTIVO, PROGRESO, FINALIZADO");
        }

        tournamentValidator.validarTransicionEstado(torneo.getEstado(), estadoEnum);
        torneo.setEstado(estadoEnum);

        return torneo;
    }
}
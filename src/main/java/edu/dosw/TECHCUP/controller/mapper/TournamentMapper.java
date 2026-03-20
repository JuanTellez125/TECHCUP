package edu.dosw.TECHCUP.controller.mapper;

import edu.dosw.TECHCUP.controller.dto.TournamentResponseDTO;
import edu.dosw.TECHCUP.core.model.Torneo;


public class TournamentMapper {
    public static TournamentResponseDTO toResponseDTO(Torneo torneo) {
        TournamentResponseDTO dto = new TournamentResponseDTO();
        dto.setFechaInicio(torneo.getFechaInicio());
        dto.setFechaFinal(torneo.getFechaFinal());
        dto.setCantidadEquipos(torneo.getCantidadEquipos());
        dto.setCostoInscripcion(torneo.getCostoInscripcion());
        dto.setEstado(torneo.getEstado());
        return dto;
    }
}

package edu.dosw.TECHCUP.util;

import edu.dosw.TECHCUP.dto.TorneoResponseDTO;
import edu.dosw.TECHCUP.model.Torneo;

public class TorneoMapper {
    public static TorneoResponseDTO toResponseDTO(Torneo torneo) {
        TorneoResponseDTO dto = new TorneoResponseDTO();
        dto.setFechaInicio(torneo.getFechaInicio());
        dto.setFechaFinal(torneo.getFechaFinal());
        dto.setCantidadEquipos(torneo.getCantidadEquipos());
        dto.setCostoInscripcion(torneo.getCostoInscripcion());
        dto.setEstado(torneo.getEstado());
        return dto;
    }
}

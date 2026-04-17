package edu.dosw.TECHCUP.controller.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeamMemberUpdateRequestDTO {
    /** true = jugador suspendido (no disponible), false = disponible */
    private Boolean suspended;
    /** Posición del jugador: PORTERO, DEFENSA, MEDIOCAMPISTA, DELANTERO */
    private String position;
}

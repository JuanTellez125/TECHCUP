package edu.dosw.TECHCUP.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminStatsResponseDTO {
    private long torneos;
    private long equipos;
    private long partidos;
}

package edu.dosw.TECHCUP.controller.dto.response;

import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TournamentConfigResponseDTO {

    private Long id;

    private Long tournamentId;

    private String rulebook;

    private LocalDate inscriptionDeadline;

    private String schedules;

    private String sanctions;
}
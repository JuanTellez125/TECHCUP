package edu.dosw.TECHCUP.controller.dto.request;

import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TournamentConfigRequestDTO {

    private String rules;

    private LocalDate registrationCloseDate;

    private String fields;

    private String sanctions;
}
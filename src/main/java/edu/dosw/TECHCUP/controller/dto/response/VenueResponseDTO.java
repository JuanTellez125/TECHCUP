package edu.dosw.TECHCUP.controller.dto.response;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VenueResponseDTO {

    private Long id;

    private Long tournamentId;

    private String name;

    private String Location;

    private String description;
}
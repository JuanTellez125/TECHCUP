package edu.dosw.TECHCUP.controller.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VenueRequestDTO {

    @NotBlank(message = "The name of the court cannot be empty.")
    private String name;

    private String venueLocation;

    private String description;
}
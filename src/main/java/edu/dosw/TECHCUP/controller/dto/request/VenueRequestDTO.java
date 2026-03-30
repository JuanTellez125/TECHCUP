package edu.dosw.TECHCUP.controller.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VenueRequestDTO {

    @NotBlank(message = "El nombre de la cancha no puede estar vacío")
    private String name;

    private String Location;

    private String description;
}
package edu.dosw.TECHCUP.controller.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TeamRequestDTO {

    @NotBlank(message = "The team's name cannot be empty")
    private String name;

    @NotBlank(message = "The team's shield cannot be empty")
    private String shieldUrl;

    private String mainColor;

    private String secondaryColor;
}
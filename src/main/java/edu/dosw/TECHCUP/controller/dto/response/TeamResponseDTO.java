package edu.dosw.TECHCUP.controller.dto.response;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeamResponseDTO {

    private Long id;

    private Long captainId;

    private String captainName;

    private String name;

    private String shieldUrl;

    private String mainColor;

    private String secondaryColor;

    private boolean active;
}
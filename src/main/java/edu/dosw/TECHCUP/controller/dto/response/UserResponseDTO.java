package edu.dosw.TECHCUP.controller.dto.response;


import edu.dosw.TECHCUP.core.model.enums.PlayerAvailable;
import edu.dosw.TECHCUP.core.model.enums.Role;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import javax.swing.text.Position;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {

    private String id;

    private String name;

    private String email;

    private String password;

    private Byte[] userPhoto;

    private Role role;

    private int dorsal;

    private Position mainPosition;

    private Position secundaryPosition;

    private PlayerAvailable playerAvailable;

}

package edu.dosw.TECHCUP.controller.dto.request;


import edu.dosw.TECHCUP.core.model.enums.PlayerAvailable;
import edu.dosw.TECHCUP.core.model.enums.Role;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.swing.text.Position;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDTO {

    @NotNull(message = "The name cannot be blank")
    private String name;

    @NotNull(message = "The email cannot be blank")
    private String email;

    @NotNull(message = "The password cannot be blank")
    private String password;

    @NotNull(message = "The user photo cannot be blank")
    private Byte[] userPhoto;

    @NotNull(message = "The role cannot be blank")
    private Role role;

    @NotNull(message = "The dorsal cannot be blank")
    private int dorsal;

    @NotNull(message = "The mainPosition cannot be blank")
    private Position mainPosition;

    private Position secundaryPosition;

    private PlayerAvailable playerAvailable;
}

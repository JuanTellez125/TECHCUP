package edu.dosw.TECHCUP.controller.dto.response;

import edu.dosw.TECHCUP.core.model.enums.Role;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {

    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String documentId;

    private Role userType;

    private boolean active;
}
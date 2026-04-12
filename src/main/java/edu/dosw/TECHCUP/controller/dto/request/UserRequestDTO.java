package edu.dosw.TECHCUP.controller.dto.request;

import edu.dosw.TECHCUP.core.model.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDTO {

    @NotBlank(message = "The name cannot be empty.")
    private String firstName;

    @NotBlank(message = "The surname cannot be empty.")
    private String lastName;

    @NotBlank(message = "The email cannot be empty.")
    @Email(message = "The email is not in a valid format.")
    private String email;

    @NotBlank(message = "The password cannot be empty.")
    private String password;

    @NotBlank(message = "The document cannot be empty.")
    private String documentId;

    @NotNull(message = "The user type cannot be empty")
    private Role userType;
}
package edu.dosw.TECHCUP.controller.dto.request;


import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Builder
public class UserRequestDTO {

    @NotNull(message = "The name cannot be blank")
    private String name;

    @NotNull(message = "The email cannot be blank")
    private String email;

    @NotNull(message = "The password cannot be blank")
    private String password;

    @NotNull(message = "The user photo cannot be blank")
    private Byte[] userPhoto;

}

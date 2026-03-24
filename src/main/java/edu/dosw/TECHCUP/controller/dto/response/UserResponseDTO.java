package edu.dosw.TECHCUP.controller.dto.response;


import jakarta.validation.constraints.NotNull;
import lombok.*;

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

}

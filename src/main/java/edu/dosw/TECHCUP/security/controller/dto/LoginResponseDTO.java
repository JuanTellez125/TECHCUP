package edu.dosw.TECHCUP.security.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

//Sirve para enviar los datos cuando se hace el login

@Getter
@Setter
@AllArgsConstructor
public class LoginResponseDTO {

    private Long userId;
    private String token;
    private String type;
    private String email;
    private String firstName;
    private String lastName;
    private String userType;
}



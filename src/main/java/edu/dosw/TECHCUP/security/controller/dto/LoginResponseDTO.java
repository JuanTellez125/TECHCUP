package edu.dosw.TECHCUP.security.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

//Sirve para enviar los datos cuando se hace el login

@Getter
@Setter
@AllArgsConstructor
public class LoginResponseDTO {

    private String token;
    private String type;//en este caso siempre se usa el Barer
    private String email;
}



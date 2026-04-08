package edu.dosw.TECHCUP.security.controller.dto;

import lombok.*;

@Getter
@Setter
public class LoginRequestDTO {
    private String email;
    private String password;
}

package edu.dosw.TECHCUP.controller.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponseDTO {
    private long id;
    private String name;
    private String email;
    private String password;

}

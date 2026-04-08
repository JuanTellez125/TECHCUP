package edu.dosw.TECHCUP.security.controller.dto;

import edu.dosw.TECHCUP.core.model.enums.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequestDTO {
     private String firstName;
     private String lastName;
     private String email;
     private String password;
     private String documentId;
     private Role userType;
}

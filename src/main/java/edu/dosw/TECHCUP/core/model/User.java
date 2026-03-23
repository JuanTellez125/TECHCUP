package edu.dosw.TECHCUP.core.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class User {
    private long id;
    private String name;
    private String email;
    private String password;
    private Byte[] picture;

}

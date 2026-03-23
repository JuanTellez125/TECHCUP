package edu.dosw.TECHCUP.core.model;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class User {
    private long id;
    private String name;
    private String email;
    private String password;
    private Byte[] picture;

}

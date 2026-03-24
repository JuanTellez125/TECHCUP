package edu.dosw.TECHCUP.core.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;


@Data
@Builder
public class User {
    @Id
    private long id;

    private String name;

    @Indexed(unique = true)

    private String email;

    private String password;

    private Byte[] userPhoto;

    private Role role;

}

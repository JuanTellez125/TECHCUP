package edu.dosw.TECHCUP.core.model;

import edu.dosw.TECHCUP.core.model.enums.PlayerAvailable;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import edu.dosw.TECHCUP.core.model.enums.Role;
import edu.dosw.TECHCUP.core.model.enums.Position;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table (name = "users")
@Entity
public class User {

    @Id
    private String id;

    private String name;

    private String email;

    private String password;

    private Byte[] userPhoto;

    private Role role;

    private int dorsal;

    private Position mainPosition;

    private Position secundaryPosition;

    private PlayerAvailable playerAvailable;

    private Team team;

    private String gender;

    private String identification;

    private int semester;



}
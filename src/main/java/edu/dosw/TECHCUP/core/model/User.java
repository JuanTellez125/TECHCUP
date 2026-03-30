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
public class User {
    private Long user_id;
    private String email;
    private String documentId;
    private String password;
    private String firstName;
    private String lastName;
    private Role userType;
    private boolean active;


}
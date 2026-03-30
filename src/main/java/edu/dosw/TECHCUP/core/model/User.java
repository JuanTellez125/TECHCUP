package edu.dosw.TECHCUP.core.model;

import edu.dosw.TECHCUP.core.model.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
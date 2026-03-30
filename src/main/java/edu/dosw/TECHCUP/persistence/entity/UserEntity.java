package edu.dosw.TECHCUP.persistence.entity;

import edu.dosw.TECHCUP.core.model.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data

@Builder

@AllArgsConstructor

@NoArgsConstructor

@Entity

@Table(name = "users")

public class UserEntity {

    @Id

    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long user_id;

    @Column(unique = true, nullable = false)

    private String email;

    @Column(unique = true, nullable = false)

    private String documentId;

    private String password;

    private String firstName;

    private String lastName;

    @Enumerated(EnumType.STRING)

    private Role userType;

    private boolean active;

}

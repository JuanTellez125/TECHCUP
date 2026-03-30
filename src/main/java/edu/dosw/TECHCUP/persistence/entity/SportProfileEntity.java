package edu.dosw.TECHCUP.persistence.entity;

import edu.dosw.TECHCUP.core.model.User;
import edu.dosw.TECHCUP.core.model.enums.Position;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "sport_profiles")
public class SportProfileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sportProfile_id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private Position primaryPosition;

    @Enumerated(EnumType.STRING)
    private Position secondaryPosition;

    private int jerseyNumber;
    private String photoUrl;
    private LocalDate birthDate;
    private boolean available;
}

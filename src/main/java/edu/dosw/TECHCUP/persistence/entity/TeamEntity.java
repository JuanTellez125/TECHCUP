package edu.dosw.TECHCUP.persistence.entity;

import edu.dosw.TECHCUP.core.model.TeamMember;
import edu.dosw.TECHCUP.core.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "teams")
public class TeamEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "captain_id")
    private User captain;

    @Column(unique = true)
    private String name;

    private String shieldUrl;
    private String mainColor;
    private String secondaryColor;
    private boolean active;

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL)
    private List<TeamMember> members;
}

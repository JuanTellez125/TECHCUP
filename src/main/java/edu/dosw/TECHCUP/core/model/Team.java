package edu.dosw.TECHCUP.core.model;


import edu.dosw.TECHCUP.core.model.enums.TeamStatus;
import edu.dosw.TECHCUP.core.model.LineUp;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

import javax.sound.sampled.Line;
import java.awt.*;
import java.util.List;

@Data
@Entity
@Builder
public class Team {
    private Long id;
    private User captain;
    private String name;
    private String shieldUrl;
    private boolean active;
    private List<TeamMember> members;
}
package edu.dosw.TECHCUP.core.model;


import edu.dosw.TECHCUP.core.model.enums.TeamStatus;
//import edu.dosw.TECHCUP.core.model.LineUp;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import javax.sound.sampled.Line;
import java.awt.*;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Team {
    private Long id;
    private User captain;
    private String name;
    private String shieldUrl;
    private boolean active;
    private List<TeamMember> members;
}
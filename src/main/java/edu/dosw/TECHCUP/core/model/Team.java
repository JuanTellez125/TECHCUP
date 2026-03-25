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
@Table(name = "teams")
public class Team {

    @Id
    private String id;

    private String captainId;

    private Byte[] teamShield;

    private String teamName;

    private Color mainColor;

    private Color secundaryColor;

    private List<User> members;

    private LineUp lineUp;

    private TeamStatus teamStatus;



    
}
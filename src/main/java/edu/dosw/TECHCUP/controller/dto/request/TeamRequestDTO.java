package edu.dosw.TECHCUP.controller.dto.request;

import edu.dosw.TECHCUP.core.model.LineUp;
import edu.dosw.TECHCUP.core.model.User;
import edu.dosw.TECHCUP.core.model.enums.TeamStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.awt.*;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TeamRequestDTO {

    private String captain_id;

    private Byte[] teamShield;

    private String teamName;

    private Color mainColor;

    private Color secundaryColor;

    private List<User> members;

    private LineUp lineUp;

    private TeamStatus teamStatus;

}

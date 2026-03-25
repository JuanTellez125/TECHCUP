package edu.dosw.TECHCUP.controller.dto.request;

import edu.dosw.TECHCUP.core.model.Team;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LineUpRequestDTO {

    private List<String> titular;

    private List<String> substitutes;

    private List<String> suspended;

    private Team team;

}

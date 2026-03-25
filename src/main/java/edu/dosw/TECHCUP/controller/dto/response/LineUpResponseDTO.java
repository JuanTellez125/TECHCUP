package edu.dosw.TECHCUP.controller.dto.response;

import edu.dosw.TECHCUP.core.model.Team;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class LineUpResponseDTO {

    private String id;

    private List<String> titular;

    private List<String> substitutes;

    private List<String> suspended;

    private Team team;

}

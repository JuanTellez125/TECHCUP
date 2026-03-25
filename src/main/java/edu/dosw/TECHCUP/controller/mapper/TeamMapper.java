package edu.dosw.TECHCUP.controller.mapper;

import edu.dosw.TECHCUP.controller.dto.response.TeamResponseDTO;
import edu.dosw.TECHCUP.controller.dto.response.TournamentResponseDTO;
import edu.dosw.TECHCUP.core.model.Team;
import edu.dosw.TECHCUP.core.model.Tournament;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TeamMapper {

    Team toEntity(TeamResponseDTO dto);

    TeamResponseDTO toDto(Team entity);

}

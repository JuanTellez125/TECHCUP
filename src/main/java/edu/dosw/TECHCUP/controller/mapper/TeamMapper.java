package edu.dosw.TECHCUP.controller.mapper;

import edu.dosw.TECHCUP.controller.dto.request.TeamRequestDTO;
import edu.dosw.TECHCUP.controller.dto.response.TeamResponseDTO;
import edu.dosw.TECHCUP.core.model.Team;
import edu.dosw.TECHCUP.persistence.entity.TeamEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TeamMapper {

    @Mapping(source = "captain.userId",    target = "captainId")
    @Mapping(source = "captain.firstName", target = "captainName")
    TeamResponseDTO toDto(Team team);

    Team toModel(TeamRequestDTO dto);
}
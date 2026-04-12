package edu.dosw.TECHCUP.persistence.mapper;

import edu.dosw.TECHCUP.controller.dto.response.TeamResponseDTO;
import edu.dosw.TECHCUP.core.model.Team;
import edu.dosw.TECHCUP.persistence.entity.TeamEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TeamPersistenceMapper {

    Team toModel(TeamEntity teamEntity);

    TeamEntity toEntity(Team team);
}
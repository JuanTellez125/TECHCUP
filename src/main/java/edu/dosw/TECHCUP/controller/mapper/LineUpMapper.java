package edu.dosw.TECHCUP.controller.mapper;

import edu.dosw.TECHCUP.controller.dto.request.LineUpRequestDTO;
import edu.dosw.TECHCUP.controller.dto.response.LineUpResponseDTO;
import edu.dosw.TECHCUP.core.model.LineUp;
import edu.dosw.TECHCUP.persistence.entity.LineUpEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LineUpMapper {

    @Mapping(source = "lineUpId",         target = "id")
    @Mapping(source = "match.matchId",   target = "matchId")
    @Mapping(source = "team.id",         target = "teamId")
    @Mapping(source = "team.name",       target = "teamName")
    @Mapping(source = "user.userId",     target = "userId")
    @Mapping(source = "user.firstName",  target = "userName")
    //@Mapping(source = "role.name",       target = "role")
    LineUpResponseDTO toDto(LineUp lineUp);

    LineUp toModel(LineUpRequestDTO dto);
}
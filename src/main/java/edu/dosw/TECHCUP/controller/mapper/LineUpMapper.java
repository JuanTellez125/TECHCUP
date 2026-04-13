package edu.dosw.TECHCUP.controller.mapper;

import edu.dosw.TECHCUP.controller.dto.response.LineUpResponseDTO;
import edu.dosw.TECHCUP.core.model.LineUp;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LineUpMapper {

    @Mapping(source = "lineUpId",        target = "id")
    @Mapping(source = "match.matchId",   target = "matchId")
    @Mapping(source = "team.id",         target = "teamId")
    @Mapping(source = "team.name",       target = "teamName")
    @Mapping(source = "user.userId",     target = "userId")
    @Mapping(source = "user.firstName",  target = "userName")
    LineUpResponseDTO toDto(LineUp lineUp);

    List<LineUpResponseDTO> toDtoList(List<LineUp> lineUps);
}
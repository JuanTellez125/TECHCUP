package edu.dosw.TECHCUP.controller.mapper;

import edu.dosw.TECHCUP.controller.dto.request.MatchEventRequestDTO;
import edu.dosw.TECHCUP.controller.dto.response.MatchEventResponseDTO;
import edu.dosw.TECHCUP.core.model.MatchEvent;
import edu.dosw.TECHCUP.persistence.entity.MatchEventEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MatchEventMapper {

    @Mapping(source = "matchEvent_id",    target = "id")
    @Mapping(source = "match.match_id",   target = "matchId")
    @Mapping(source = "user.user_id",     target = "userId")
    @Mapping(source = "user.firstName",   target = "userName")
    @Mapping(source = "team.id",          target = "teamId")
    @Mapping(source = "team.name",        target = "teamName")
   // @Mapping(source = "eventType.name",   target = "eventType")
    MatchEventResponseDTO toDto(MatchEvent matchEvent);

    MatchEvent toModel(MatchEventRequestDTO dto);
}
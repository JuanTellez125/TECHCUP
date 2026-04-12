package edu.dosw.TECHCUP.persistence.mapper;

import edu.dosw.TECHCUP.controller.dto.response.MatchEventResponseDTO;
import edu.dosw.TECHCUP.core.model.MatchEvent;
import edu.dosw.TECHCUP.persistence.entity.MatchEventEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MatchEventPersistenceMapper {

    MatchEvent toModel(MatchEventEntity matchEventEntity);

    MatchEventEntity toEntity(MatchEvent matchEvent);
}
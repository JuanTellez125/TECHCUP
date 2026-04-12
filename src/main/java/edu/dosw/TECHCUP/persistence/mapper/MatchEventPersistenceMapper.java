package edu.dosw.TECHCUP.persistence.mapper;

import edu.dosw.TECHCUP.core.model.MatchEvent;
import edu.dosw.TECHCUP.persistence.entity.MatchEventEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MatchEventPersistenceMapper {

    @Mapping(source = "matchEvent_id", target = "matchEventId")
    MatchEvent toModel(MatchEventEntity matchEventEntity);

    @Mapping(source = "matchEventId", target = "matchEvent_id")
    MatchEventEntity toEntity(MatchEvent matchEvent);
}
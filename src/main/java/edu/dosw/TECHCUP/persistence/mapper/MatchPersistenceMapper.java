package edu.dosw.TECHCUP.persistence.mapper;

import edu.dosw.TECHCUP.core.model.Match;
import edu.dosw.TECHCUP.persistence.entity.MatchEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {TournamentPersistenceMapper.class, TeamPersistenceMapper.class, VenuePersistenceMapper.class, UserPersistenceMapper.class})
public interface MatchPersistenceMapper {

    @Mapping(source = "match_id", target = "matchId")
    @Mapping(target = "result",   ignore = true)
    @Mapping(target = "events",   ignore = true)
    @Mapping(target = "lineUps",  ignore = true)
    @Mapping(target = "status",   ignore = true)
    Match toModel(MatchEntity matchEntity);

    @Mapping(source = "matchId", target = "match_id")
    @Mapping(target = "result",   ignore = true)
    @Mapping(target = "events",   ignore = true)
    @Mapping(target = "lineups",  ignore = true)
    @Mapping(target = "status",   ignore = true)
    MatchEntity toEntity(Match match);
}
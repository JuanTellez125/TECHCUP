package edu.dosw.TECHCUP.persistence.mapper;

import edu.dosw.TECHCUP.core.model.MatchResult;
import edu.dosw.TECHCUP.persistence.entity.MatchResultEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {MatchPersistenceMapper.class, UserPersistenceMapper.class})
public interface MatchResultPersistenceMapper {

    @Mapping(source = "matchResult_id", target = "matchResultId")
    MatchResult toModel(MatchResultEntity matchResultEntity);

    @Mapping(source = "matchResultId", target = "matchResult_id")
    MatchResultEntity toEntity(MatchResult matchResult);
}
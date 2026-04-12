package edu.dosw.TECHCUP.persistence.mapper;

import edu.dosw.TECHCUP.controller.dto.response.MatchResponseDTO;
import edu.dosw.TECHCUP.core.model.Match;
import edu.dosw.TECHCUP.persistence.entity.MatchEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MatchPersistenceMapper {

    @Mapping(source = "match_id", target = "matchId")
    Match toModel(MatchEntity matchEntity);

    @Mapping(source = "matchId", target = "match_id")
    MatchEntity toEntity(Match match);
}
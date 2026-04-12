package edu.dosw.TECHCUP.persistence.mapper;

import edu.dosw.TECHCUP.controller.dto.response.MatchResponseDTO;
import edu.dosw.TECHCUP.core.model.Match;
import edu.dosw.TECHCUP.persistence.entity.MatchEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MatchPersistenceMapper {

    Match toModel(MatchEntity matchEntity);

    MatchEntity toEntity(Match match);
}
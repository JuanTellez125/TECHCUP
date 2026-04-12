package edu.dosw.TECHCUP.persistence.mapper;

import edu.dosw.TECHCUP.controller.dto.response.MatchResultResponseDTO;
import edu.dosw.TECHCUP.core.model.MatchResult;
import edu.dosw.TECHCUP.persistence.entity.MatchResultEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MatchResultPersistenceMapper {

    MatchResult toModel(MatchResultEntity matchResultEntity);

    MatchResultEntity toEntity(MatchResult matchResult);
}
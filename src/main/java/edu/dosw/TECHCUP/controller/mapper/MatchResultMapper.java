package edu.dosw.TECHCUP.controller.mapper;

import edu.dosw.TECHCUP.controller.dto.response.MatchResultResponseDTO;
import edu.dosw.TECHCUP.core.model.MatchResult;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MatchResultMapper {

    @Mapping(source = "matchResult_id",   target = "id")
    @Mapping(source = "match.match_id",   target = "matchId")
    MatchResultResponseDTO toDto(MatchResult matchResult);
}
package edu.dosw.TECHCUP.controller.mapper;

import edu.dosw.TECHCUP.controller.dto.request.MatchResultRequestDTO;
import edu.dosw.TECHCUP.controller.dto.response.MatchResultResponseDTO;
import edu.dosw.TECHCUP.core.model.MatchResult;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MatchResultMapper {

    @Mapping(source = "matchResultId",   target = "id")
    @Mapping(source = "match.matchId",   target = "matchId")
    MatchResultResponseDTO toDto(MatchResult matchResult);

    // match, registeredBy y registeredAt los completa el servicio tras buscarlos en BD
    @Mapping(target = "matchResultId",   ignore = true)
    @Mapping(target = "match",           ignore = true)
    @Mapping(target = "registeredBy",    ignore = true)
    @Mapping(target = "registeredAt",    ignore = true)
    MatchResult toModel(MatchResultRequestDTO dto);
}
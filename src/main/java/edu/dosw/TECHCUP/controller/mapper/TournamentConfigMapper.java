package edu.dosw.TECHCUP.controller.mapper;

import edu.dosw.TECHCUP.controller.dto.request.TournamentConfigRequestDTO;
import edu.dosw.TECHCUP.controller.dto.response.TournamentConfigResponseDTO;
import edu.dosw.TECHCUP.core.model.TournamentConfig;
import edu.dosw.TECHCUP.persistence.entity.TournamentConfigEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TournamentConfigMapper {

    @Mapping(source = "tournamentConfigId",       target = "id")
    @Mapping(source = "tournament.tournamentId",  target = "tournamentId")
    TournamentConfigResponseDTO toDto(TournamentConfig config);

    TournamentConfig toModel(TournamentConfigRequestDTO dto);
}
package edu.dosw.TECHCUP.controller.mapper;

import edu.dosw.TECHCUP.controller.dto.response.TournamentConfigResponseDTO;
import edu.dosw.TECHCUP.core.model.TournamentConfig;
import edu.dosw.TECHCUP.persistence.entity.TournamentConfigEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TournamentConfigMapper {

    @Mapping(source = "tournamentConfig_id",    target = "id")
    @Mapping(source = "tournament.tournament_id", target = "tournamentId")
    TournamentConfigResponseDTO toDto(TournamentConfigEntity config);
}
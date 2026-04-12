package edu.dosw.TECHCUP.persistence.mapper;

import edu.dosw.TECHCUP.controller.dto.response.TournamentConfigResponseDTO;
import edu.dosw.TECHCUP.core.model.TournamentConfig;
import edu.dosw.TECHCUP.persistence.entity.TournamentConfigEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TournamentConfigPersistenceMapper {

    TournamentConfig toModel(TournamentConfigEntity tournamentConfigEntity);

    TournamentConfigEntity toEntity(TournamentConfig tournamentConfig);
}
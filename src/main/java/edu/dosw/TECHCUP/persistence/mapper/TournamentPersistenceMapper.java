package edu.dosw.TECHCUP.persistence.mapper;

import edu.dosw.TECHCUP.controller.dto.response.TournamentResponseDTO;
import edu.dosw.TECHCUP.core.model.Tournament;
import edu.dosw.TECHCUP.persistence.entity.TournamentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TournamentPersistenceMapper {

    Tournament toModel(TournamentEntity tournamentEntity);

    TournamentEntity toEntity(Tournament tournament);

}
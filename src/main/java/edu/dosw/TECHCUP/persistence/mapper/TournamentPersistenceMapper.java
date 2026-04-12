package edu.dosw.TECHCUP.persistence.mapper;

import edu.dosw.TECHCUP.core.model.Tournament;
import edu.dosw.TECHCUP.persistence.entity.TournamentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UserPersistenceMapper.class})
public interface TournamentPersistenceMapper {

    @Mapping(source = "tournament_id", target = "tournamentId")
    @Mapping(target = "config", ignore = true)
    Tournament toModel(TournamentEntity tournamentEntity);

    @Mapping(source = "tournamentId", target = "tournament_id")
    @Mapping(target = "config", ignore = true)
    TournamentEntity toEntity(Tournament tournament);

}
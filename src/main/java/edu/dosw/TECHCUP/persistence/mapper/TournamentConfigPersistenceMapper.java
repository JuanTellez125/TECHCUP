package edu.dosw.TECHCUP.persistence.mapper;

import edu.dosw.TECHCUP.core.model.TournamentConfig;
import edu.dosw.TECHCUP.persistence.entity.TournamentConfigEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {TournamentPersistenceMapper.class})
public interface TournamentConfigPersistenceMapper {

    @Mapping(source = "tournamentConfig_id", target = "tournamentConfigId")
    TournamentConfig toModel(TournamentConfigEntity tournamentConfigEntity);

    @Mapping(source = "tournamentConfigId", target = "tournamentConfig_id")
    TournamentConfigEntity toEntity(TournamentConfig tournamentConfig);
}
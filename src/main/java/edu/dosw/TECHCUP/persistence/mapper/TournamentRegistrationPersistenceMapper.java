package edu.dosw.TECHCUP.persistence.mapper;

import edu.dosw.TECHCUP.core.model.TournamentRegistration;
import edu.dosw.TECHCUP.persistence.entity.TournamentRegistrationEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TournamentRegistrationPersistenceMapper {

    @Mapping(source = "tournamentRegistration_id", target = "tournamentRegistrationId")
    TournamentRegistration toModel(TournamentRegistrationEntity tournamentRegistrationEntity);

    @Mapping(source = "tournamentRegistrationId", target = "tournamentRegistration_id")
    TournamentRegistrationEntity toEntity(TournamentRegistration tournamentRegistration);
}
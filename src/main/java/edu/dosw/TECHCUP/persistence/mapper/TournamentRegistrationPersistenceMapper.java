package edu.dosw.TECHCUP.persistence.mapper;

import edu.dosw.TECHCUP.controller.dto.response.TournamentRegistrationResponseDTO;
import edu.dosw.TECHCUP.core.model.TournamentRegistration;
import edu.dosw.TECHCUP.persistence.entity.TournamentRegistrationEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TournamentRegistrationPersistenceMapper {

    TournamentRegistration toModel(TournamentRegistrationEntity tournamentRegistrationEntity);

    TournamentRegistrationEntity toEntity(TournamentRegistration tournamentRegistration);
}
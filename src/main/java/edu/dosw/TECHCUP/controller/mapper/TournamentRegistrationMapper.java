package edu.dosw.TECHCUP.controller.mapper;

import edu.dosw.TECHCUP.controller.dto.request.TournamentRegistrationRequestDTO;
import edu.dosw.TECHCUP.controller.dto.response.TournamentRegistrationResponseDTO;
import edu.dosw.TECHCUP.core.model.TournamentRegistration;
import edu.dosw.TECHCUP.persistence.entity.TournamentRegistrationEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TournamentRegistrationMapper {

    @Mapping(source = "tournamentRegistrationId",      target = "id")
    @Mapping(source = "tournament.tournamentId",       target = "tournamentId")
    @Mapping(source = "team.id",                      target = "teamId")
    @Mapping(source = "team.name",                    target = "teamName")
    TournamentRegistrationResponseDTO toDto(TournamentRegistration registration);

    TournamentRegistration toModel(TournamentRegistrationRequestDTO dto);
}
package edu.dosw.TECHCUP.controller.mapper;

import edu.dosw.TECHCUP.controller.dto.request.TournamentRequestDTO;
import edu.dosw.TECHCUP.controller.dto.response.TournamentResponseDTO;
import edu.dosw.TECHCUP.core.model.Tournament;
import edu.dosw.TECHCUP.persistence.entity.TournamentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TournamentMapper {

    @Mapping(source = "tournament_id",       target = "id")
    @Mapping(source = "organizer.user_id",   target = "organizerId")
    @Mapping(source = "organizer.firstName", target = "organizerName")
    TournamentResponseDTO toDto(Tournament tournament);

    Tournament toModel(TournamentRequestDTO dto);
}
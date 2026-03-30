package edu.dosw.TECHCUP.controller.mapper;

import edu.dosw.TECHCUP.controller.dto.response.VenueResponseDTO;
import edu.dosw.TECHCUP.core.model.Venue;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface VenueMapper {

    @Mapping(source = "vanue_id",               target = "id")
    @Mapping(source = "tournament.tournament_id", target = "tournamentId")
    @Mapping(source = "Location",               target = "location")
    VenueResponseDTO toDto(Venue venue);
}
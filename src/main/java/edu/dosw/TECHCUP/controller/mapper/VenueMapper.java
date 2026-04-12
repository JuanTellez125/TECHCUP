package edu.dosw.TECHCUP.controller.mapper;

import edu.dosw.TECHCUP.controller.dto.request.VenueRequestDTO;
import edu.dosw.TECHCUP.controller.dto.response.VenueResponseDTO;
import edu.dosw.TECHCUP.core.model.Venue;
import edu.dosw.TECHCUP.persistence.entity.VenueEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface VenueMapper {

    @Mapping(source = "venueId",                 target = "id")
    @Mapping(source = "tournament.tournamentId",  target = "tournamentId")
    @Mapping(source = "location",                target = "venueLocation")
    VenueResponseDTO toDto(Venue venue);

    Venue toModel(VenueRequestDTO dto);
}
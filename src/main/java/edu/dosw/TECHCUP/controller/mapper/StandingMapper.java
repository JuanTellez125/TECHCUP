package edu.dosw.TECHCUP.controller.mapper;

import edu.dosw.TECHCUP.controller.dto.response.StandingResponseDTO;
import edu.dosw.TECHCUP.core.model.Standing;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface StandingMapper {

    @Mapping(source = "standing_id",                  target = "id")
    @Mapping(source = "tournament.tournament_id",     target = "tournamentId")
    @Mapping(source = "team.id",                      target = "teamId")
    @Mapping(source = "team.name",                    target = "teamName")
    StandingResponseDTO toDto(Standing standing);
}
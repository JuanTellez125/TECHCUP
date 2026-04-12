package edu.dosw.TECHCUP.controller.mapper;

import edu.dosw.TECHCUP.controller.dto.request.MatchRequestDTO;
import edu.dosw.TECHCUP.controller.dto.response.MatchResponseDTO;
import edu.dosw.TECHCUP.core.model.Match;
import edu.dosw.TECHCUP.persistence.entity.MatchEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MatchMapper {

    @Mapping(source = "match_id",               target = "id")
    @Mapping(source = "tournament.tournament_id", target = "tournamentId")
    @Mapping(source = "team1.id",               target = "team1Id")
    @Mapping(source = "team1.name",             target = "team1Name")
    @Mapping(source = "team2.id",               target = "team2Id")
    @Mapping(source = "team2.name",             target = "team2Name")
    @Mapping(source = "vanue.vanue_id",         target = "venueId")
    @Mapping(source = "vanue.name",             target = "venueName")
    @Mapping(source = "referee.user_id",        target = "refereeId")
    @Mapping(source = "referee.firstName",      target = "refereeName")
    @Mapping(target = "status",                 ignore = true)
    MatchResponseDTO toDto(Match match);

    Match toModel(MatchRequestDTO dto);
}
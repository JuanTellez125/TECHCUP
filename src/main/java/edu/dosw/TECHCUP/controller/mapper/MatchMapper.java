package edu.dosw.TECHCUP.controller.mapper;

import edu.dosw.TECHCUP.controller.dto.request.MatchRequestDTO;
import edu.dosw.TECHCUP.controller.dto.response.MatchResponseDTO;
import edu.dosw.TECHCUP.core.model.Match;
import edu.dosw.TECHCUP.persistence.entity.MatchEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MatchMapper {

    @Mapping(source = "matchId",                 target = "id")
    @Mapping(source = "tournament.tournamentId", target = "tournamentId")
    @Mapping(source = "team1.id",               target = "team1Id")
    @Mapping(source = "team1.name",             target = "team1Name")
    @Mapping(source = "team2.id",               target = "team2Id")
    @Mapping(source = "team2.name",             target = "team2Name")
    @Mapping(source = "venue.venueId",           target = "venueId")
    @Mapping(source = "venue.name",             target = "venueName")
    @Mapping(source = "referee.userId",          target = "refereeId")
    @Mapping(source = "referee.firstName",      target = "refereeName")
    MatchResponseDTO toDto(Match match);

    @Mapping(target = "tournament.tournamentId", source = "tournamentId")
    @Mapping(target = "team1.id",               source = "team1Id")
    @Mapping(target = "team2.id",               source = "team2Id")
    @Mapping(target = "venue.venueId",           source = "venueId")
    @Mapping(target = "referee.userId",          source = "refereeId")
    Match toModel(MatchRequestDTO dto);
}
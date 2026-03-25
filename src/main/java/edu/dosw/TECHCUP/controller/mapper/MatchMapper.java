package edu.dosw.TECHCUP.controller.mapper;

import edu.dosw.TECHCUP.controller.dto.response.MatchResponseDTO;
import edu.dosw.TECHCUP.controller.dto.response.TournamentResponseDTO;
import edu.dosw.TECHCUP.core.model.Match;
import edu.dosw.TECHCUP.core.model.Tournament;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MatchMapper {

    Match toEntity(MatchResponseDTO dto);

    MatchResponseDTO toDto(Match entity);

}

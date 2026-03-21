package edu.dosw.TECHCUP.controller.mapper;

import edu.dosw.TECHCUP.controller.dto.TournamentResponseDTO;
import edu.dosw.TECHCUP.core.model.Tournament;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TournamentMapper {

    TournamentMapper INSTANCE = Mappers.getMapper(TournamentMapper.class);

    TournamentResponseDTO toResponseDTO(Tournament tournament);
}
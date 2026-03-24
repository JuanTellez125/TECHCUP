package edu.dosw.TECHCUP.controller.mapper;

import edu.dosw.TECHCUP.controller.dto.response.TournamentResponseDTO;
import edu.dosw.TECHCUP.controller.dto.response.UserResponseDTO;
import edu.dosw.TECHCUP.core.model.Tournament;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface TournamentMapper {

    Tournament toEntity(TournamentResponseDTO dto);

    TournamentResponseDTO toDto(Tournament entity);
}
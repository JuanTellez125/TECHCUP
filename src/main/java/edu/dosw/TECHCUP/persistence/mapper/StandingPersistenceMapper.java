package edu.dosw.TECHCUP.persistence.mapper;

import edu.dosw.TECHCUP.controller.dto.response.StandingResponseDTO;
import edu.dosw.TECHCUP.core.model.Standing;
import edu.dosw.TECHCUP.persistence.entity.StandingEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface StandingPersistenceMapper {

    Standing  toModel(StandingEntity standingEntity);

    StandingEntity toEntity(Standing standing);
}
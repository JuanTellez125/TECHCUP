package edu.dosw.TECHCUP.persistence.mapper;

import edu.dosw.TECHCUP.core.model.Standing;
import edu.dosw.TECHCUP.persistence.entity.StandingEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface StandingPersistenceMapper {

    @Mapping(source = "standing_id", target = "standingId")
    Standing toModel(StandingEntity standingEntity);

    @Mapping(source = "standingId", target = "standing_id")
    StandingEntity toEntity(Standing standing);
}
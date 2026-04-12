package edu.dosw.TECHCUP.persistence.mapper;

import edu.dosw.TECHCUP.core.model.Venue;
import edu.dosw.TECHCUP.persistence.entity.VenueEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface VenuePersistenceMapper {

    Venue toModel(VenueEntity venueEntity);

    VenueEntity toEntity(Venue venue);
}
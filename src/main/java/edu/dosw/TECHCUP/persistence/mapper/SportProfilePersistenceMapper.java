package edu.dosw.TECHCUP.persistence.mapper;

import edu.dosw.TECHCUP.core.model.SportProfile;
import edu.dosw.TECHCUP.persistence.entity.SportProfileEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UserPersistenceMapper.class})
public interface SportProfilePersistenceMapper {

    @Mapping(source = "sportProfile_id", target = "sportProfileId")
    SportProfile toModel(SportProfileEntity sportProfileEntity);

    @Mapping(source = "sportProfileId", target = "sportProfile_id")
    SportProfileEntity toEntity(SportProfile sportProfile);
}
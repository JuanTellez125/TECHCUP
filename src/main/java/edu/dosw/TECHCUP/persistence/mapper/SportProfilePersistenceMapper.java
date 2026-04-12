package edu.dosw.TECHCUP.persistence.mapper;

import edu.dosw.TECHCUP.controller.dto.response.SportProfileResponseDTO;
import edu.dosw.TECHCUP.core.model.SportProfile;
import edu.dosw.TECHCUP.persistence.entity.SportProfileEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SportProfilePersistenceMapper {

    SportProfile toModel(SportProfileEntity sportProfileEntity);

    SportProfileEntity toEntity(SportProfile sportProfile);
}
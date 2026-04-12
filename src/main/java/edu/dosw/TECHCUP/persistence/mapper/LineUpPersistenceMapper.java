package edu.dosw.TECHCUP.persistence.mapper;

import edu.dosw.TECHCUP.controller.dto.response.LineUpResponseDTO;
import edu.dosw.TECHCUP.core.model.LineUp;
import edu.dosw.TECHCUP.persistence.entity.LineUpEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LineUpPersistenceMapper {

    LineUp toModel(LineUpEntity lineUpEntity);

    LineUpEntity toEntity(LineUp lineUp);
}
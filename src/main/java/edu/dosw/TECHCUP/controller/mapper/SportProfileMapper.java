package edu.dosw.TECHCUP.controller.mapper;

import edu.dosw.TECHCUP.controller.dto.response.SportProfileResponseDTO;
import edu.dosw.TECHCUP.core.model.SportProfile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SportProfileMapper {

    @Mapping(source = "sportProfile_id", target = "id")
    @Mapping(source = "user.user_id",    target = "userId")
    @Mapping(source = "user.firstName",  target = "firstName")
    @Mapping(source = "user.lastName",   target = "lastName")
    SportProfileResponseDTO toDto(SportProfile sportProfile);
}
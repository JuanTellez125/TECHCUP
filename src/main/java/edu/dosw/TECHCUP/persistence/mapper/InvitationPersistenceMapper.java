package edu.dosw.TECHCUP.persistence.mapper;

import edu.dosw.TECHCUP.controller.dto.response.InvitationResponseDTO;
import edu.dosw.TECHCUP.core.model.Invitation;
import edu.dosw.TECHCUP.persistence.entity.InvitationEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface InvitationPersistenceMapper {

    Invitation toModel(InvitationEntity invitationEntity);

    InvitationEntity toEntity(Invitation invitation);
}
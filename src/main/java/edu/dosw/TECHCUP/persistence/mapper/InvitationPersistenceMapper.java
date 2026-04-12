package edu.dosw.TECHCUP.persistence.mapper;

import edu.dosw.TECHCUP.core.model.Invitation;
import edu.dosw.TECHCUP.persistence.entity.InvitationEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface InvitationPersistenceMapper {

    @Mapping(source = "invitation_id", target = "invitationId")
    Invitation toModel(InvitationEntity invitationEntity);

    @Mapping(source = "invitationId", target = "invitation_id")
    InvitationEntity toEntity(Invitation invitation);
}
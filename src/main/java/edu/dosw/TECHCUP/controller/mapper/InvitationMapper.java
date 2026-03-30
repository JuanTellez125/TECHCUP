package edu.dosw.TECHCUP.controller.mapper;

import edu.dosw.TECHCUP.controller.dto.response.InvitationResponseDTO;
import edu.dosw.TECHCUP.core.model.Invitation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface InvitationMapper {

    @Mapping(source = "invitation_id",          target = "id")
    @Mapping(source = "team.id",                target = "teamId")
    @Mapping(source = "team.name",              target = "teamName")
    @Mapping(source = "invitedUser.user_id",    target = "invitedUserId")
    @Mapping(source = "invitedUser.firstName",  target = "invitedUserName")
    @Mapping(source = "invitedBy.user_id",      target = "invitedById")
    @Mapping(source = "invitedBy.firstName",    target = "invitedByName")
    InvitationResponseDTO toDto(Invitation invitation);
}
package edu.dosw.TECHCUP.persistence.mapper;

import edu.dosw.TECHCUP.core.model.TeamMember;
import edu.dosw.TECHCUP.persistence.entity.TeamMemberEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UserPersistenceMapper.class})
public interface TeamMemberPersistenceMapper {

    @Mapping(source = "id", target = "teamMemberId")
    @Mapping(target = "team", ignore = true)
    TeamMember toModel(TeamMemberEntity entity);

    @Mapping(source = "teamMemberId", target = "id")
    @Mapping(target = "team", ignore = true)
    TeamMemberEntity toEntity(TeamMember model);
}

package edu.dosw.TECHCUP.persistence.mapper;

import edu.dosw.TECHCUP.core.model.LineUp;
import edu.dosw.TECHCUP.persistence.entity.LineUpEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {MatchPersistenceMapper.class, TeamPersistenceMapper.class, UserPersistenceMapper.class})
public interface LineUpPersistenceMapper {

    @Mapping(source = "lineUp_id", target = "lineUpId")
    LineUp toModel(LineUpEntity lineUpEntity);

    @Mapping(source = "lineUpId", target = "lineUp_id")
    LineUpEntity toEntity(LineUp lineUp);
}
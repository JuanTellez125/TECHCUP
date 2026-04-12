package edu.dosw.TECHCUP.persistence.mapper;

import edu.dosw.TECHCUP.core.model.User;
import edu.dosw.TECHCUP.persistence.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserPersistenceMapper {

    @Mapping(source = "user_id", target = "userId")
    User toModel(UserEntity entity);

    @Mapping(source = "userId", target = "user_id")
    UserEntity toEntity(User model);
}

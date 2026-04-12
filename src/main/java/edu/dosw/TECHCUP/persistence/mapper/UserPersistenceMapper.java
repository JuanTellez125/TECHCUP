package edu.dosw.TECHCUP.persistence.mapper;

import edu.dosw.TECHCUP.core.model.User;
import edu.dosw.TECHCUP.persistence.entity.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserPersistenceMapper {

    User toModel(UserEntity entity);

    UserEntity toEntity(User model);
}

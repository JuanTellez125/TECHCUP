package edu.dosw.TECHCUP.persistence.repository;

import edu.dosw.TECHCUP.core.model.enums.Role;
import edu.dosw.TECHCUP.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findByUserType(Role userType);

    List<UserEntity> findAllByUserType(Role userType);

    boolean existsByEmail(String email);

    boolean existsByDocumentId(String documentId);
}

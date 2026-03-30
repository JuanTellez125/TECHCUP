package edu.dosw.TECHCUP.persistence.repository;

import edu.dosw.TECHCUP.core.model.User;
import edu.dosw.TECHCUP.core.model.enums.Role;
import edu.dosw.TECHCUP.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,Long> {

    Optional<UserEntity> findById(Long id);

    User findByRole(Role role);

    Optional<UserEntity> findByRoleAndId(Role role, Long id);

    Optional<UserEntity> findByEmail(String email);

    List<UserEntity> findAll();

    List<UserEntity> findAllByRole(Role role);

    Optional<UserEntity> findAllByUserType(Role role);

    boolean existsByEmail(String email);

    boolean existsByDocumentId(String documentId);
}
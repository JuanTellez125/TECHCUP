package edu.dosw.TECHCUP.persistence.repository;

import edu.dosw.TECHCUP.core.model.User;
import edu.dosw.TECHCUP.core.model.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findById(Long id);

    User findByRole(Role role);

    Optional<User> findByRoleAndId(Role role, Long id);

    Optional<User> findByEmail(String email);

    List<User> findAll();

    List<User> findAllByRole(Role role);

    Optional<User> findAllByUserType(Role role);

    boolean existsByEmail(String email);

    boolean existsByDocumentId(String documentId);
}
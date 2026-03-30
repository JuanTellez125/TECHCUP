package edu.dosw.TECHCUP.persistence.repository;

import edu.dosw.TECHCUP.core.model.User;
import edu.dosw.TECHCUP.core.model.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByDocumentId(String documentId);

    Optional<User> findByUserTypeAndUser_id(Role role, Long id);

    List<User> findAllByUserType(Role role);

    boolean existsByEmail(String email);

    boolean existsByDocumentId(String documentId);
}
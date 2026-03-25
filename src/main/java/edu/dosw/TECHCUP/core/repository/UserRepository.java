package edu.dosw.TECHCUP.core.repository;

import edu.dosw.TECHCUP.core.model.enums.Role;
import edu.dosw.TECHCUP.core.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,String> {

    Optional<User> findById(String id);

    User findByRole(Role role);

    Optional<User> findByRoleAndId(Role role, String id);

    Optional<User> findByEmail(String email);

    List<User> findAll();

    List<User> findAllByRole(Role role);
}
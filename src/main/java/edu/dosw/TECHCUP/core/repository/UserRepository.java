package edu.dosw.TECHCUP.core.repository;

import edu.dosw.TECHCUP.core.model.Role;
import edu.dosw.TECHCUP.core.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User,String> {

    Optional<User> findById(String id);

    User findByRole(Role role);

    Optional<User> findByRoleAndId(Role role, String id);

    Optional<User> findByEmail(String email);

    List<User> findAll();

    List<User> findAllByRole(Role role);
}

package edu.dosw.TECHCUP.core.service;

import edu.dosw.TECHCUP.core.model.User;

import java.util.Optional;

public interface UserService {

    User createUser(User user);

    User updateUser(Long id, User user);

    void deleteUser(Long id);

    Optional<User> findByEmail(String email);
}

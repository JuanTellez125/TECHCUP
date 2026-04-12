package edu.dosw.TECHCUP.core.service.impl;

import edu.dosw.TECHCUP.core.exception.UserNotFoundException;
import edu.dosw.TECHCUP.core.exception.UserValidationException;
import edu.dosw.TECHCUP.core.model.User;
import edu.dosw.TECHCUP.core.model.enums.Role;
import edu.dosw.TECHCUP.core.service.UserService;
import edu.dosw.TECHCUP.core.validator.UserValidator;
import edu.dosw.TECHCUP.persistence.entity.UserEntity;
import edu.dosw.TECHCUP.persistence.mapper.UserPersistenceMapper;
import edu.dosw.TECHCUP.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CaptainService implements UserService {

    private final UserRepository userRepository;
    private final UserPersistenceMapper userPersistenceMapper;
    private final UserValidator userValidator;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public User createUser(User user) {
        userValidator.validate(user);

        if (userRepository.existsByEmail(user.getEmail()))
            throw new UserValidationException("A user with the following email address already exists: " + user.getEmail());
        if (userRepository.existsByDocumentId(user.getDocumentId()))
            throw new UserValidationException("A user already exists with the document: " + user.getDocumentId());

        UserEntity captain = UserEntity.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .password(passwordEncoder.encode(user.getPassword()))
                .documentId(user.getDocumentId())
                .userType(Role.CAPTAIN)
                .active(true)
                .build();

        UserEntity saved = userRepository.save(captain);
        log.info("Captain created with ID: {}", saved.getUser_id());
        return userPersistenceMapper.toModel(saved);
    }

    @Transactional
    @Override
    public User updateUser(Long id, User user) {
        UserEntity entity = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        entity.setFirstName(user.getFirstName());
        entity.setLastName(user.getLastName());
        entity.setEmail(user.getEmail());
        if (user.getPassword() != null && !user.getPassword().isBlank()) {
            entity.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        return userPersistenceMapper.toModel(userRepository.save(entity));
    }

    @Transactional
    @Override
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id))
            throw new UserNotFoundException(id);
        userRepository.deleteById(id);
        log.info("Eliminated Captain: {}", id);
    }

    public User getCaptainById(Long id) {
        return userPersistenceMapper.toModel(userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id)));
    }

    @Override
    public Optional<User> findByEmail(String email) {
        log.debug("Looking for a captain by email: {}", email);
        return userRepository.findByEmail(email)
                .map(userPersistenceMapper::toModel);
    }
}

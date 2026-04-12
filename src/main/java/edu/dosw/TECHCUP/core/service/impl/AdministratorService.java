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
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdministratorService implements UserService {

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

        Role role = (user.getUserType() != null) ? user.getUserType() : Role.ADMINISTRATOR;

        UserEntity admin = UserEntity.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .password(passwordEncoder.encode(user.getPassword()))
                .documentId(user.getDocumentId())
                .userType(role)
                .active(true)
                .build();

        UserEntity saved = userRepository.save(admin);
        log.info("User created with ID: {} and role: {}", saved.getUser_id(), saved.getUserType());
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
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(userPersistenceMapper::toModel);
    }

    @Transactional
    public User assignRole(Long adminId, Long targetUserId, Role newRole) {
        userRepository.findById(adminId)
                .filter(u -> u.getUserType() == Role.ADMINISTRATOR)
                .orElseThrow(() -> new UserValidationException("Only an administrator can assign roles."));

        UserEntity target = userRepository.findById(targetUserId)
                .orElseThrow(() -> new UserNotFoundException(targetUserId));

        target.setUserType(newRole);
        log.info("Role {} assigned to the user {}", newRole, targetUserId);
        return userPersistenceMapper.toModel(userRepository.save(target));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userPersistenceMapper::toModel)
                .collect(Collectors.toList());
    }

    public User getAdminById(Long id) {
        return userPersistenceMapper.toModel(userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id)));
    }
}

package edu.dosw.TECHCUP.core.service.impl;

import edu.dosw.TECHCUP.controller.dto.request.SportProfileRequestDTO;
import edu.dosw.TECHCUP.controller.dto.response.SportProfileResponseDTO;
import edu.dosw.TECHCUP.controller.mapper.SportProfileMapper;
import edu.dosw.TECHCUP.core.exception.SportProfileException;
import edu.dosw.TECHCUP.core.exception.UserNotFoundException;
import edu.dosw.TECHCUP.core.exception.UserValidationException;
import edu.dosw.TECHCUP.core.model.User;
import edu.dosw.TECHCUP.core.model.enums.Role;
import edu.dosw.TECHCUP.core.service.UserService;
import edu.dosw.TECHCUP.core.validator.UserValidator;
import edu.dosw.TECHCUP.persistence.entity.SportProfileEntity;
import edu.dosw.TECHCUP.persistence.entity.UserEntity;
import edu.dosw.TECHCUP.persistence.mapper.SportProfilePersistenceMapper;
import edu.dosw.TECHCUP.persistence.mapper.UserPersistenceMapper;
import edu.dosw.TECHCUP.persistence.repository.SportProfileRepository;
import edu.dosw.TECHCUP.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class PlayerService implements UserService {

    private final UserRepository userRepository;
    private final UserPersistenceMapper userPersistenceMapper;
    private final SportProfileRepository sportProfileRepository;
    private final SportProfileMapper sportProfileMapper;
    private final SportProfilePersistenceMapper sportProfilePersistenceMapper;
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

        UserEntity player = UserEntity.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .password(passwordEncoder.encode(user.getPassword()))
                .documentId(user.getDocumentId())
                .userType(Role.PLAYER)
                .active(true)
                .build();

        UserEntity saved = userRepository.save(player);
        log.info("Player created with ID: {}", saved.getUser_id());
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
        log.info("Eliminated player: {}", id);
    }

    @Transactional
    public SportProfileResponseDTO setAvailability(Long userId, boolean available) {
        SportProfileEntity profile = sportProfileRepository.findByUser_User_id(userId)
                .orElseThrow(() -> new SportProfileException(userId));
        profile.setAvailable(available);
        return sportProfileMapper.toDto(sportProfilePersistenceMapper.toModel(sportProfileRepository.save(profile)));
    }

    @Transactional
    public SportProfileResponseDTO getSportProfile(Long userId) {
        SportProfileEntity profile = sportProfileRepository.findByUser_User_id(userId)
                .orElseThrow(() -> new SportProfileException(userId));
        return sportProfileMapper.toDto(sportProfilePersistenceMapper.toModel(profile));
    }

    public User getPlayerById(Long id) {
        return userPersistenceMapper.toModel(userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id)));
    }

    public List<User> getAllPlayers() {
        return userRepository.findAllByUserType(Role.PLAYER).stream()
                .map(userPersistenceMapper::toModel)
                .collect(Collectors.toList());
    }

    @Transactional
    public SportProfileResponseDTO saveSportProfile(Long userId, SportProfileRequestDTO dto) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        SportProfileEntity profile = sportProfileRepository.findByUser_User_id(userId)
                .orElse(SportProfileEntity.builder().user(user).build());

        profile.setPrimaryPosition(dto.getPrimaryPosition());
        profile.setAvailable(dto.isAvailable());

        return sportProfileMapper.toDto(sportProfilePersistenceMapper.toModel(sportProfileRepository.save(profile)));
    }

    @Override
    public Optional<User> findByEmail(String email) {
        log.debug("Looking for a player by email: {}", email);
        return userRepository.findByEmail(email)
                .map(userPersistenceMapper::toModel);
    }
}

package edu.dosw.TECHCUP.core.service.impl;

import edu.dosw.TECHCUP.controller.dto.request.SportProfileRequestDTO;
import edu.dosw.TECHCUP.controller.dto.request.UserRequestDTO;
import edu.dosw.TECHCUP.controller.dto.response.SportProfileResponseDTO;
import edu.dosw.TECHCUP.controller.dto.response.UserResponseDTO;
import edu.dosw.TECHCUP.controller.mapper.SportProfileMapper;
import edu.dosw.TECHCUP.controller.mapper.UserMapper;
import edu.dosw.TECHCUP.core.exception.SportProfileException;
import edu.dosw.TECHCUP.core.exception.UserNotFoundException;
import edu.dosw.TECHCUP.core.exception.UserValidationException;
import edu.dosw.TECHCUP.core.model.SportProfile;
import edu.dosw.TECHCUP.core.model.enums.Role;
import edu.dosw.TECHCUP.persistence.entity.SportProfileEntity;
import edu.dosw.TECHCUP.persistence.entity.UserEntity;
import edu.dosw.TECHCUP.persistence.repository.UserRepository;
import edu.dosw.TECHCUP.core.service.UserService;
import edu.dosw.TECHCUP.core.validator.UserValidator;
import edu.dosw.TECHCUP.persistence.repository.SportProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class PlayerService implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final SportProfileRepository sportProfileRepository;
    private final SportProfileMapper sportProfileMapper;
    private final UserValidator userValidator;

    @Transactional
    @Override
    public UserResponseDTO createUser(UserRequestDTO dto) {
        userValidator.validate(dto);

        if (userRepository.existsByEmail(dto.getEmail()))
            throw new UserValidationException("Ya existe un usuario con el email: " + dto.getEmail());
        if (userRepository.existsByDocumentId(dto.getDocumentId()))
            throw new UserValidationException("Ya existe un usuario con el documento: " + dto.getDocumentId());

        UserEntity player = UserEntity.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .documentId(dto.getDocumentId())
                .userType(Role.PLAYER)
                .active(true)
                .build();

        UserEntity saved = userRepository.save(player);
        log.info("Jugador creado con ID: {}", saved.getUser_id());
        return userMapper.toDto(saved);
    }

    @Transactional
    @Override
    public UserResponseDTO updateUser(Long id, UserRequestDTO dto) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());

        return userMapper.toDto(userRepository.save(user));
    }

    @Transactional
    @Override
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id))
            throw new UserNotFoundException(id);
        userRepository.deleteById(id);
        log.info("Jugador eliminado: {}", id);
    }

    @Transactional
    public SportProfileResponseDTO setAvailability(Long userId, boolean available) {
        SportProfileEntity profile = sportProfileRepository.findByUser_User_id(userId)
                .orElseThrow(() -> new SportProfileException( userId));

        profile.setAvailable(available);
        return sportProfileMapper.toDto(sportProfileRepository.save(profile));
    }

    @Transactional
    public SportProfileResponseDTO getSportProfile(Long userId) {
        SportProfileEntity profile = sportProfileRepository.findByUser_User_id(userId)
                .orElseThrow(() -> new SportProfileException( userId));
        return sportProfileMapper.toDto(profile);
    }

    @Transactional
    public UserResponseDTO getPlayerById(Long id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        return userMapper.toDto(user);
    }

    @Transactional
    public List<UserResponseDTO> getAllPlayers() {
        return userRepository.findAllByRole(Role.PLAYER)
                .stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

}

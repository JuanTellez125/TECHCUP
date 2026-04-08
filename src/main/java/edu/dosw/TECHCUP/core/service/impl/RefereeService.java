package edu.dosw.TECHCUP.core.service.impl;

import edu.dosw.TECHCUP.controller.dto.request.UserRequestDTO;
import edu.dosw.TECHCUP.controller.dto.response.UserResponseDTO;
import edu.dosw.TECHCUP.controller.mapper.UserMapper;
import edu.dosw.TECHCUP.core.exception.UserNotFoundException;
import edu.dosw.TECHCUP.core.exception.UserValidationException;
import edu.dosw.TECHCUP.core.model.User;
import edu.dosw.TECHCUP.core.model.enums.Role;
import edu.dosw.TECHCUP.persistence.entity.UserEntity;
import edu.dosw.TECHCUP.persistence.repository.UserRepository;
import edu.dosw.TECHCUP.core.service.UserService;
import edu.dosw.TECHCUP.core.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import edu.dosw.TECHCUP.core.model.User;
import java.util.Optional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class RefereeService implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserValidator userValidator;

    @Transactional
    @Override
    public UserResponseDTO createUser(UserRequestDTO dto) {
        userValidator.validate(dto);

        if (userRepository.existsByEmail(dto.getEmail()))
            throw new UserValidationException("Ya existe un usuario con el email: " + dto.getEmail());

        UserEntity referee = UserEntity.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .documentId(dto.getDocumentId())
                .userType(Role.REFEREE)
                .active(true)
                .build();

        UserEntity saved = userRepository.save(referee);
        log.info("Árbitro creado con ID: {}", saved.getUser_id());
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
    }

    public UserResponseDTO getRefereeById(Long id) {
        return userMapper.toDto(userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id)));
    }

    public List<UserResponseDTO> getAllReferees() {
        return userRepository.findAllByUserType(Role.REFEREE)
                .stream().map(userMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public Optional<User> findByEmail(String email) {
        log.debug("Buscando árbitro por email: {}", email);
        return userRepository.findByEmail(email)
                .map(entity -> User.builder()
                        .user_id(entity.getUser_id())
                        .email(entity.getEmail())
                        .password(entity.getPassword())
                        .firstName(entity.getFirstName())
                        .lastName(entity.getLastName())
                        .documentId(entity.getDocumentId())
                        .userType(entity.getUserType())
                        .active(entity.isActive())
                        .build());
    }
}

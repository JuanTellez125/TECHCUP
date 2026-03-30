package edu.dosw.TECHCUP.core.service.impl;

import edu.dosw.TECHCUP.controller.dto.request.UserRequestDTO;
import edu.dosw.TECHCUP.controller.dto.response.UserResponseDTO;
import edu.dosw.TECHCUP.controller.mapper.UserMapper;
import edu.dosw.TECHCUP.core.exception.UserNotFoundException;
import edu.dosw.TECHCUP.core.exception.UserValidationException;
import edu.dosw.TECHCUP.core.model.User;
import edu.dosw.TECHCUP.core.model.enums.Role;
import edu.dosw.TECHCUP.core.repository.UserRepository;
import edu.dosw.TECHCUP.core.service.UserService;
import edu.dosw.TECHCUP.core.util.IdGeneratorUtil;
import edu.dosw.TECHCUP.core.validator.UserValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdministratorService implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserValidator userValidator;

    @Transactional
    @Override
    public UserResponseDTO createUser(UserRequestDTO dto) {
        userValidator.validate(dto);

        if (userRepository.existsByEmail(dto.getEmail()))
            throw new UserValidationException("Ya existe un usuario con el email: " + dto.getEmail());

        User admin = User.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .documentId(dto.getDocumentId())
                .userType(Role.ADMINISTRATOR)
                .active(true)
                .build();

        User saved = userRepository.save(admin);
        log.info("Administrador creado con ID: {}", saved.getUser_id());
        return userMapper.toDto(saved);
    }

    @Transactional
    @Override
    public UserResponseDTO updateUser(Long id, UserRequestDTO dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(String.valueOf(id)));
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
            throw new UserNotFoundException(String.valueOf(id));
        userRepository.deleteById(id);
    }

    @Transactional
    public UserResponseDTO assignRole(Long adminId, Long targetUserId, Role newRole) {
        userRepository.findById(adminId)
                .filter(u -> u.getUserType() == Role.ADMINISTRATOR)
                .orElseThrow(() -> new UserValidationException("Solo un administrador puede asignar roles."));

        User target = userRepository.findById(targetUserId)
                .orElseThrow(() -> new UserNotFoundException(String.valueOf(targetUserId)));

        target.setUserType(newRole);
        log.info("Rol {} asignado al usuario {}", newRole, targetUserId);
        return userMapper.toDto(userRepository.save(target));
    }

    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toDto).collect(Collectors.toList());
    }

    public UserResponseDTO getAdminById(Long id) {
        return userMapper.toDto(userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(String.valueOf(id))));
    }
}


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
    public UserResponseDTO createUser(UserRequestDTO dto) {
        userValidator.validate(dto);

        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new UserValidationException(
                    "Ya existe un usuario con el email: " + dto.getEmail());
        }

        User admin = User.builder()
                .id(IdGeneratorUtil.generateId())
                .name(dto.getName())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .role(Role.ADMINISTRATOR)
                .build();

        User saved = userRepository.save(admin);
        log.info("Administrador creado con ID: {}", saved.getId());
        return userMapper.toDto(saved);
    }

    @Transactional
    public UserResponseDTO updateUser(String id, UserRequestDTO dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());

        User updated = userRepository.save(user);
        log.info("Administrador actualizado: {}", id);
        return userMapper.toDto(updated);
    }

    @Transactional
    public void deleteUser(String id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException(id);
        }
        userRepository.deleteById(id);
        log.info("Administrador eliminado: {}", id);
    }

    public UserResponseDTO getAdminById(String id) {
        User admin = userRepository.findByRoleAndId(Role.ADMINISTRATOR, id)
                .orElseThrow(() -> new UserNotFoundException(id));
        return userMapper.toDto(admin);
    }

    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public UserResponseDTO assignRole(String adminId, String targetUserId, Role newRole) {
        userRepository.findByRoleAndId(Role.ADMINISTRATOR, adminId)
                .orElseThrow(() -> new UserValidationException("Solo un administrador puede asignar roles."));

        User target = userRepository.findById(targetUserId).orElseThrow(() -> new UserNotFoundException(targetUserId));

        target.setRole(newRole);
        User updated = userRepository.save(target);
        log.info("Rol actualizado para usuario {}: {}", targetUserId, newRole);
        return userMapper.toDto(updated);
    }
}


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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public UserResponseDTO createUser(UserRequestDTO dto) {
        userValidator.validate(dto);

        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new UserValidationException(
                    "Ya existe un usuario con el email: " + dto.getEmail());
        }

        User referee = User.builder()
                .id(IdGeneratorUtil.generateId())
                .name(dto.getName())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .role(Role.REFEREE)
                .build();

        User saved = userRepository.save(referee);
        log.info("Árbitro creado con ID: {}", saved.getId());
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
        log.info("Árbitro actualizado: {}", id);
        return userMapper.toDto(updated);
    }

    @Transactional
    public void deleteUser(String id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException(id);
        }
        userRepository.deleteById(id);
        log.info("Árbitro eliminado: {}", id);
    }

    public UserResponseDTO getRefereeById(String id) {
        User referee = userRepository.findByRoleAndId(Role.REFEREE, id)
                .orElseThrow(() -> new UserNotFoundException(id));
        return userMapper.toDto(referee);
    }

    public List<UserResponseDTO> getAllReferees() {
        return userRepository.findAllByRole(Role.REFEREE)
                .stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }
}

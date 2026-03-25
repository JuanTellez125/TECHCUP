package edu.dosw.TECHCUP.core.service.impl;

import edu.dosw.TECHCUP.controller.dto.request.UserRequestDTO;
import edu.dosw.TECHCUP.controller.dto.response.UserResponseDTO;
import edu.dosw.TECHCUP.controller.mapper.UserMapper;
import edu.dosw.TECHCUP.core.exception.UserNotFoundException;
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

import java.util.Base64;

@Service
@Slf4j
@RequiredArgsConstructor
public class CaptainService implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserValidator userValidator;

    @Transactional
    @Override
    public UserResponseDTO createUser(UserRequestDTO dto) {
        userValidator.validate(dto);

        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new edu.dosw.TECHCUP.core.exception.UserValidationException(
                    "Ya existe un usuario con el email: " + dto.getEmail());
        }

        User captain = User.builder()
                .id(IdGeneratorUtil.generateId())
                .name(dto.getName())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .userPhoto(dto.getUserPhoto())
                .role(Role.CAPTAIN)
                .dorsal(dto.getDorsal())
                .mainPosition(dto.getMainPosition())
                .secondaryPosition(dto.getSecondaryPosition())
                .playerAvailable(dto.getPlayerAvailable())
                .build();

        User saved = userRepository.save(captain);
        log.info("Capitán creado con ID: {}", saved.getId());
        return userMapper.toDto(saved);
    }

    @Transactional
    @Override
    public UserResponseDTO updateUser(String id, UserRequestDTO dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setDorsal(dto.getDorsal());
        user.setMainPosition(dto.getMainPosition());
        user.setSecondaryPosition(dto.getSecondaryPosition());
        user.setPlayerAvailable(dto.getPlayerAvailable());

        User updated = userRepository.save(user);
        log.info("Capitán actualizado: {}", id);
        return userMapper.toDto(updated);
    }

    @Transactional
    @Override
    public void deleteUser(String id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException(id);
        }
        userRepository.deleteById(id);
        log.info("Capitán eliminado: {}", id);
    }

    public UserResponseDTO getCaptainById(String id) {
        User captain = userRepository.findByRoleAndId(Role.CAPTAIN, id)
                .orElseThrow(() -> new UserNotFoundException(id));
        return userMapper.toDto(captain);
    }
}

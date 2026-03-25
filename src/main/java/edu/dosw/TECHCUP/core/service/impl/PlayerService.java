package edu.dosw.TECHCUP.core.service.impl;

import edu.dosw.TECHCUP.controller.dto.request.UserRequestDTO;
import edu.dosw.TECHCUP.controller.dto.response.UserResponseDTO;
import edu.dosw.TECHCUP.controller.mapper.UserMapper;
import edu.dosw.TECHCUP.core.exception.UserNotFoundException;
import edu.dosw.TECHCUP.core.model.enums.PlayerAvailable;
import edu.dosw.TECHCUP.core.model.enums.Role;
import edu.dosw.TECHCUP.core.model.User;
import edu.dosw.TECHCUP.core.repository.UserRepository;
import edu.dosw.TECHCUP.core.service.UserService;
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

    @Transactional
    public UserResponseDTO createUser(UserRequestDTO dto) {
        User player = User.builder()

                .name(dto.getName())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .userPhoto(dto.getUserPhoto())
                .role(Role.PLAYER)
                .dorsal(dto.getDorsal())
                .mainPosition(dto.getMainPosition())
                .secondaryPosition(dto.getSecondaryPosition())
                .playerAvailable(PlayerAvailable.AVAILABLE)
                .build();

        User saved = userRepository.save(player);
        log.info("Jugador creado con ID: {}", saved.getId());
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

        if (dto.getPlayerAvailable() != null) {
            user.setPlayerAvailable(dto.getPlayerAvailable());
        }

        User updated = userRepository.save(user);
        log.info("Jugador actualizado: {}", id);
        return userMapper.toDto(updated);
    }

    @Transactional
    public void deleteUser(String id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException(id);
        }
        userRepository.deleteById(id);
        log.info("Jugador eliminado: {}", id);
    }

    @Transactional
    public UserResponseDTO setAvailability(String playerId, PlayerAvailable availability) {
        User user = userRepository.findById(playerId)
                .orElseThrow(() -> new UserNotFoundException(playerId));
        user.setPlayerAvailable(availability);
        return userMapper.toDto(userRepository.save(user));
    }

    public UserResponseDTO getPlayerById(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        return userMapper.toDto(user);
    }

    public List<UserResponseDTO> getAllPlayers() {
        return userRepository.findAllByRole(Role.PLAYER)
                .stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

}

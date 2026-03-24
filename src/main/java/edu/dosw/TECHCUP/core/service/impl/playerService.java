package edu.dosw.TECHCUP.core.service.impl;

import edu.dosw.TECHCUP.controller.dto.request.UserRequestDTO;
import edu.dosw.TECHCUP.controller.dto.response.UserResponseDTO;
import edu.dosw.TECHCUP.controller.mapper.UserMapper;
import edu.dosw.TECHCUP.core.exception.UserNotFoundException;
import edu.dosw.TECHCUP.core.model.Role;
import edu.dosw.TECHCUP.core.model.User;
import edu.dosw.TECHCUP.core.repository.UserRepository;
import edu.dosw.TECHCUP.core.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class playerService implements UserService {

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
                .build();

        User savedUser = userRepository.save(player);
        return userMapper.toDto(savedUser);
    }

    public UserResponseDTO updateUser(String id,UserRequestDTO dto) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setDorsal(dto.getDorsal());
        user.setMainPosition(dto.getMainPosition());
        userRepository.save(user);
        return userMapper.toDto(user);
    }

    public void deleteUser(String id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException(id);
        }
        userRepository.deleteById(id);
    }

    public UserResponseDTO consultPlayerInformation(String id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));

        return userMapper.toDto(user);
    }

}

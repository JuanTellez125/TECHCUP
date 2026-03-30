package edu.dosw.TECHCUP.core.service;

import edu.dosw.TECHCUP.controller.dto.request.UserRequestDTO;
import edu.dosw.TECHCUP.controller.dto.response.UserResponseDTO;

public interface UserService {

    UserResponseDTO createUser(UserRequestDTO dto);

    UserResponseDTO updateUser(Long id, UserRequestDTO dto);

    void deleteUser(Long id);
}
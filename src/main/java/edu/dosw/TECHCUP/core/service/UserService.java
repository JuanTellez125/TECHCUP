package edu.dosw.TECHCUP.core.service;

import edu.dosw.TECHCUP.controller.dto.request.UserRequestDTO;
import edu.dosw.TECHCUP.controller.dto.response.UserResponseDTO;
import edu.dosw.TECHCUP.core.exception.UserNotFoundException;
import edu.dosw.TECHCUP.core.model.User;
import edu.dosw.TECHCUP.core.validator.UserValidator;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public interface UserService {

    UserResponseDTO createUser(UserRequestDTO dto);

    UserResponseDTO updateUser(String id, UserRequestDTO dto);

    void deleteUser(String id);

}
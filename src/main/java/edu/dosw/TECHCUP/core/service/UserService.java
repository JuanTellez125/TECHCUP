package edu.dosw.TECHCUP.core.service;

import edu.dosw.TECHCUP.controller.dto.UserRequestDTO;
import edu.dosw.TECHCUP.core.exception.UserNotFoundException;
import edu.dosw.TECHCUP.core.model.User;
import edu.dosw.TECHCUP.core.model.UserBasicBuilder;
import edu.dosw.TECHCUP.core.model.UserFactory;
import edu.dosw.TECHCUP.core.validator.UserValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {

    private final Map<Long, User> users = new HashMap<>();
    private Long contadorId = 1L;

    private final UserValidator userValidator = new UserValidator();
    private final UserFactory userFactory = new UserFactory();

    public User crearUsuario(UserRequestDTO request) {
        log.debug("Starting user creation - email: {}", request.getEmail());
        userValidator.validar(request);
        log.debug("Validation passed for new user");
        User user = new User();
        UserBasicBuilder builder = new UserBasicBuilder(user);
        userFactory.createUser(builder);

        builder.buildName(request.getName());
        builder.buildEmail(request.getEmail());
        builder.buildPassword(request.getPassword());

        user.setId(contadorId);
        users.put(contadorId, user);
        contadorId++;
        log.info("User created with id: {}", user.getId());

        return user;
    }

    public List<User> listarUsuarios() {
        log.debug("Listing users - total in memory: {}", users.size());
        return users.values().stream()
                .collect(Collectors.toList());
    }

    public User obtenerUsuarioPorId(Long id) {
        log.debug("Looking for user with id: {}", id);
        User user = users.get(id);
        if (user == null) {
            log.warn("User with id {} not found in the system", id);
            throw new UserNotFoundException(id);
        }
        return user;
    }
}
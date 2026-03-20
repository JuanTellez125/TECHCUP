package edu.dosw.TECHCUP.core.service;

import edu.dosw.TECHCUP.controller.dto.UserRequestDTO;
import edu.dosw.TECHCUP.core.exception.UserNotFoundException;
import edu.dosw.TECHCUP.core.model.User;
import edu.dosw.TECHCUP.core.model.UserBasicBuilder;
import edu.dosw.TECHCUP.core.model.UserFactory;
import edu.dosw.TECHCUP.core.validator.UserValidator;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final Map<Long, User> users = new HashMap<>();
    private Long contadorId = 1L;

    private final UserValidator userValidator = new UserValidator();
    private final UserFactory userFactory = new UserFactory();

    public User crearUsuario(UserRequestDTO request) {
        userValidator.validar(request);
        User user = new User();
        UserBasicBuilder builder = new UserBasicBuilder(user);
        userFactory.createUser(builder);

        builder.buildName(request.getName());
        builder.buildEmail(request.getEmail());
        builder.buildPassword(request.getPassword());

        user.setId(contadorId);
        users.put(contadorId, user);
        contadorId++;

        return user;
    }

    public List<User> listarUsuarios() {
        return users.values().stream()
                .collect(Collectors.toList());
    }

    public User obtenerUsuarioPorId(Long id) {
        User user = users.get(id);
        if (user == null) {
            throw new UserNotFoundException(id);
        }
        return user;
    }
}
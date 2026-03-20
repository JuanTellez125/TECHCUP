package edu.dosw.TECHCUP.core.validator;


import edu.dosw.TECHCUP.controller.dto.UserRequestDTO;
import edu.dosw.TECHCUP.core.exception.UserValidationException;

public class UserValidator {

    public void validar(UserRequestDTO request) {
        if (request.getName() == null || request.getName().isBlank()) {
            throw new UserValidationException("El nombre del usuario es obligatorio.");
        }
        if (request.getEmail() == null || request.getEmail().isBlank()) {
            throw new UserValidationException("El email del usuario es obligatorio.");
        }
        if (!request.getEmail().matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            throw new UserValidationException("El formato del email no es válido.");
        }
        if (request.getPassword() == null || request.getPassword().isBlank()) {
            throw new UserValidationException("La contraseña es obligatoria.");
        }
        if (request.getPassword().length() < 6) {
            throw new UserValidationException("La contraseña debe tener al menos 6 caracteres.");
        }
    }
}

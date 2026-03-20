package edu.dosw.TECHCUP.core.validator;


import edu.dosw.TECHCUP.controller.dto.UserRequestDTO;
import edu.dosw.TECHCUP.core.exception.UserValidationException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserValidator {

    public void validar(UserRequestDTO request) {
        if (request.getName() == null || request.getName().isBlank()) {
           log.warn("Validation failed: username is null or empty");
            throw new UserValidationException("El nombre del usuario es obligatorio.");
        }
        if (request.getEmail() == null || request.getEmail().isBlank()) {
            log.warn("Validation failed: null or empty email");
            throw new UserValidationException("El email del usuario es obligatorio.");
        }
        if (!request.getEmail().matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            log.warn("Validation failed: Invalid email format - '{}'", request.getEmail());
            throw new UserValidationException("El formato del email no es válido.");
        }
        if (request.getPassword() == null || request.getPassword().isBlank()) {
            log.warn("Validation failed: null or empty password");
            throw new UserValidationException("La contraseña es obligatoria.");
        }
        if (request.getPassword().length() < 6) {
            log.warn("Validation failed: password with less than 6 characters (length: {})", request.getPassword().length());
            throw new UserValidationException("La contraseña debe tener al menos 6 caracteres.");
        }

        log.debug("UserRequestDTO validation completed successfully");

    }
}

package edu.dosw.TECHCUP.core.validator;

import edu.dosw.TECHCUP.core.exception.UserValidationException;
import edu.dosw.TECHCUP.core.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserValidator {

    public void validate(User user) {
        validateName(user.getFirstName());
        validateEmail(user.getEmail());
        validatePassword(user.getPassword());
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new UserValidationException("El nombre del usuario es obligatorio.");
        }
    }

    private void validateEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new UserValidationException("El email del usuario es obligatorio.");
        }
        if (!email.matches("^[\\w.-]+@(escuelaing\\.edu\\.co|gmail\\.com)$")) {
            throw new UserValidationException(
                    "El email debe ser institucional (@escuelaing.edu.co) o Gmail (@gmail.com).");
        }
    }

    private void validatePassword(String password) {
        if (password == null || password.isBlank()) {
            throw new UserValidationException("La contraseña es obligatoria.");
        }
        if (password.length() < 6) {
            throw new UserValidationException("La contraseña debe tener al menos 6 caracteres.");
        }
    }

    private void validateDorsal(int dorsal) {
        if (dorsal < 1 || dorsal > 99) {
            throw new UserValidationException("El dorsal debe estar entre 1 y 99.");
        }
    }
}

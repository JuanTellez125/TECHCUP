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
            throw new UserValidationException("The username is required.");
        }
    }

    private void validateEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new UserValidationException("The user's email address is required.");
        }
        if (!email.matches("^[\\w.-]+@(escuelaing\\.edu\\.co|gmail\\.com)$")) {
            throw new UserValidationException(
                    "The email must be institutional (@escuelaing.edu.co) or Gmail (@gmail.com).");
        }
    }

    private void validatePassword(String password) {
        if (password == null || password.isBlank()) {
            throw new UserValidationException("A password is required.");
        }
        if (password.length() < 6) {
            throw new UserValidationException("The password must be at least 6 characters long.");
        }
    }

    private void validateDorsal(int dorsal) {
        if (dorsal < 1 || dorsal > 99) {
            throw new UserValidationException("The number must be between 1 and 99.");
        }
    }
}

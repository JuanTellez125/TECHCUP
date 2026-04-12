package edu.dosw.TECHCUP.core.validator;

import edu.dosw.TECHCUP.core.exception.UserValidationException;
import edu.dosw.TECHCUP.core.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class UserValidatorTest {

    private UserValidator validator;

    @BeforeEach
    void setUp() {
        validator = new UserValidator();
    }

    // ─── validate() ───────────────────────────────────────────────────────────

    @Test
    void validate_validUser_doesNotThrow() {
        User user = buildUser("Juan", "juan@gmail.com", "password123");
        assertThatCode(() -> validator.validate(user)).doesNotThrowAnyException();
    }

    @Test
    void validate_institutionalEmail_doesNotThrow() {
        User user = buildUser("Ana", "ana@escuelaing.edu.co", "securePass");
        assertThatCode(() -> validator.validate(user)).doesNotThrowAnyException();
    }

    // ─── Name ─────────────────────────────────────────────────────────────────

    @Test
    void validate_nullName_throws() {
        User user = buildUser(null, "juan@gmail.com", "password123");
        assertThatThrownBy(() -> validator.validate(user))
                .isInstanceOf(UserValidationException.class)
                .hasMessageContaining("required");
    }

    @Test
    void validate_blankName_throws() {
        User user = buildUser("   ", "juan@gmail.com", "password123");
        assertThatThrownBy(() -> validator.validate(user))
                .isInstanceOf(UserValidationException.class)
                .hasMessageContaining("required");
    }

    // ─── Email ────────────────────────────────────────────────────────────────

    @Test
    void validate_nullEmail_throws() {
        User user = buildUser("Juan", null, "password123");
        assertThatThrownBy(() -> validator.validate(user))
                .isInstanceOf(UserValidationException.class)
                .hasMessageContaining("required");
    }

    @Test
    void validate_blankEmail_throws() {
        User user = buildUser("Juan", "  ", "password123");
        assertThatThrownBy(() -> validator.validate(user))
                .isInstanceOf(UserValidationException.class)
                .hasMessageContaining("required");
    }

    @Test
    void validate_invalidEmailDomain_throws() {
        User user = buildUser("Juan", "juan@hotmail.com", "password123");
        assertThatThrownBy(() -> validator.validate(user))
                .isInstanceOf(UserValidationException.class)
                .hasMessageContaining("institutional");
    }

    @Test
    void validate_emailWithoutDomain_throws() {
        User user = buildUser("Juan", "juansindominio", "password123");
        assertThatThrownBy(() -> validator.validate(user))
                .isInstanceOf(UserValidationException.class)
                .hasMessageContaining("institutional");
    }

    // ─── Password ─────────────────────────────────────────────────────────────

    @Test
    void validate_nullPassword_throws() {
        User user = buildUser("Juan", "juan@gmail.com", null);
        assertThatThrownBy(() -> validator.validate(user))
                .isInstanceOf(UserValidationException.class)
                .hasMessageContaining("required");
    }

    @Test
    void validate_blankPassword_throws() {
        User user = buildUser("Juan", "juan@gmail.com", "");
        assertThatThrownBy(() -> validator.validate(user))
                .isInstanceOf(UserValidationException.class)
                .hasMessageContaining("required");
    }

    @Test
    void validate_shortPassword_throws() {
        User user = buildUser("Juan", "juan@gmail.com", "abc");
        assertThatThrownBy(() -> validator.validate(user))
                .isInstanceOf(UserValidationException.class)
                .hasMessageContaining("6 characters");
    }

    @Test
    void validate_passwordExactlyMinLength_doesNotThrow() {
        User user = buildUser("Juan", "juan@gmail.com", "123456");
        assertThatCode(() -> validator.validate(user)).doesNotThrowAnyException();
    }

    // ─── Helpers ──────────────────────────────────────────────────────────────

    private User buildUser(String firstName, String email, String password) {
        return User.builder()
                .firstName(firstName)
                .email(email)
                .password(password)
                .build();
    }
}

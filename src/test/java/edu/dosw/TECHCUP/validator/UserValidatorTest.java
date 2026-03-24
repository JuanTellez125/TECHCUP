package edu.dosw.TECHCUP.validator;

import edu.dosw.TECHCUP.controller.dto.request.UserRequestDTO;
import edu.dosw.TECHCUP.core.exception.UserValidationException;
import edu.dosw.TECHCUP.core.validator.UserValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserValidatorTest {

    private UserValidator userValidator;

    @BeforeEach
    void setUp() {
        userValidator = new UserValidator();
    }

    @Test
    void validar_conDatosCorrectos_noLanzaExcepcion() {
        UserRequestDTO request = buildRequest("Juan", "juan@correo.com", "segura123");
        assertDoesNotThrow(() -> userValidator.validar(request));
    }

    @Test
    void validar_conNombreNulo_lanzaExcepcion() {
        UserRequestDTO request = buildRequest(null, "juan@correo.com", "segura123");
        UserValidationException ex = assertThrows(UserValidationException.class,
                () -> userValidator.validar(request));
        assertEquals("El nombre del usuario es obligatorio.", ex.getMessage());
    }

    @Test
    void validar_conNombreVacio_lanzaExcepcion() {
        UserRequestDTO request = buildRequest("   ", "juan@correo.com", "segura123");
        UserValidationException ex = assertThrows(UserValidationException.class,
                () -> userValidator.validar(request));
        assertEquals("El nombre del usuario es obligatorio.", ex.getMessage());
    }

    @Test
    void validar_conEmailNulo_lanzaExcepcion() {
        UserRequestDTO request = buildRequest("Juan", null, "segura123");
        UserValidationException ex = assertThrows(UserValidationException.class,
                () -> userValidator.validar(request));
        assertEquals("El email del usuario es obligatorio.", ex.getMessage());
    }

    @Test
    void validar_conEmailVacio_lanzaExcepcion() {
        UserRequestDTO request = buildRequest("Juan", "  ", "segura123");
        UserValidationException ex = assertThrows(UserValidationException.class,
                () -> userValidator.validar(request));
        assertEquals("El email del usuario es obligatorio.", ex.getMessage());
    }

    @Test
    void validar_conEmailSinArroba_lanzaExcepcion() {
        UserRequestDTO request = buildRequest("Juan", "juancorreo.com", "segura123");
        UserValidationException ex = assertThrows(UserValidationException.class,
                () -> userValidator.validar(request));
        assertEquals("El formato del email no es válido.", ex.getMessage());
    }

    @Test
    void validar_conEmailSinDominio_lanzaExcepcion() {
        UserRequestDTO request = buildRequest("Juan", "juan@", "segura123");
        UserValidationException ex = assertThrows(UserValidationException.class,
                () -> userValidator.validar(request));
        assertEquals("El formato del email no es válido.", ex.getMessage());
    }

    @Test
    void validar_conPasswordNulo_lanzaExcepcion() {
        UserRequestDTO request = buildRequest("Juan", "juan@correo.com", null);
        UserValidationException ex = assertThrows(UserValidationException.class,
                () -> userValidator.validar(request));
        assertEquals("La contraseña es obligatoria.", ex.getMessage());
    }

    @Test
    void validar_conPasswordVacio_lanzaExcepcion() {
        UserRequestDTO request = buildRequest("Juan", "juan@correo.com", "  ");
        UserValidationException ex = assertThrows(UserValidationException.class,
                () -> userValidator.validar(request));
        assertEquals("La contraseña es obligatoria.", ex.getMessage());
    }

    @Test
    void validar_conPasswordMenorA6Caracteres_lanzaExcepcion() {
        UserRequestDTO request = buildRequest("Juan", "juan@correo.com", "abc");
        UserValidationException ex = assertThrows(UserValidationException.class,
                () -> userValidator.validar(request));
        assertEquals("La contraseña debe tener al menos 6 caracteres.", ex.getMessage());
    }

    private UserRequestDTO buildRequest(String name, String email, String password) {
        UserRequestDTO request = new UserRequestDTO();
        request.setName(name);
        request.setEmail(email);
        request.setPassword(password);
        return request;
    }
}
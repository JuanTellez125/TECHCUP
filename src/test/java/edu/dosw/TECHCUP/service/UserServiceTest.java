package edu.dosw.TECHCUP.service;

import edu.dosw.TECHCUP.controller.dto.request.UserRequestDTO;
import edu.dosw.TECHCUP.core.exception.UserNotFoundException;
import edu.dosw.TECHCUP.core.exception.UserValidationException;
import edu.dosw.TECHCUP.core.model.User;
import edu.dosw.TECHCUP.core.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService();
    }

    @Test
    void crearUsuario_conDatosValidos_retornaUserConId() {
        UserRequestDTO request = buildRequest("Juan", "juan@correo.com", "segura123");

        User result = userService.crearUsuario(request);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Juan", result.getName());
        assertEquals("juan@correo.com", result.getEmail());
        assertEquals("segura123", result.getPassword());
    }

    @Test
    void crearUsuario_dosVeces_asignaIdsIncrementales() {
        User user1 = userService.crearUsuario(buildRequest("Juan", "juan@correo.com", "pass123"));
        User user2 = userService.crearUsuario(buildRequest("Maria", "maria@correo.com", "pass456"));

        assertEquals(1L, user1.getId());
        assertEquals(2L, user2.getId());
    }

    @Test
    void crearUsuario_conNombreNulo_lanzaValidationException() {
        UserRequestDTO request = buildRequest(null, "juan@correo.com", "segura123");

        assertThrows(UserValidationException.class,
                () -> userService.crearUsuario(request));
    }

    @Test
    void crearUsuario_conEmailInvalido_lanzaValidationException() {
        UserRequestDTO request = buildRequest("Juan", "emailinvalido", "segura123");

        assertThrows(UserValidationException.class,
                () -> userService.crearUsuario(request));
    }

    @Test
    void crearUsuario_conPasswordCorta_lanzaValidationException() {
        UserRequestDTO request = buildRequest("Juan", "juan@correo.com", "abc");

        assertThrows(UserValidationException.class,
                () -> userService.crearUsuario(request));
    }

    @Test
    void listarUsuarios_sinUsuarios_retornaListaVacia() {
        List<User> result = userService.listarUsuarios();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void listarUsuarios_conUsuariosCreados_retornaTodosLosUsuarios() {
        userService.crearUsuario(buildRequest("Juan", "juan@correo.com", "pass123"));
        userService.crearUsuario(buildRequest("Maria", "maria@correo.com", "pass456"));

        List<User> result = userService.listarUsuarios();

        assertEquals(2, result.size());
    }

    @Test
    void obtenerUsuarioPorId_conIdExistente_retornaUser() {
        userService.crearUsuario(buildRequest("Juan", "juan@correo.com", "pass123"));

        User result = userService.obtenerUsuarioPorId(1L);

        assertNotNull(result);
        assertEquals("Juan", result.getName());
    }

    @Test
    void obtenerUsuarioPorId_conIdInexistente_lanzaNotFoundException() {
        UserNotFoundException ex = assertThrows(UserNotFoundException.class,
                () -> userService.obtenerUsuarioPorId(99L));

        assertEquals("Usuario con id 99 no encontrado.", ex.getMessage());
    }

    private UserRequestDTO buildRequest(String name, String email, String password) {
        UserRequestDTO request = new UserRequestDTO();
        request.setName(name);
        request.setEmail(email);
        request.setPassword(password);
        return request;
    }
}

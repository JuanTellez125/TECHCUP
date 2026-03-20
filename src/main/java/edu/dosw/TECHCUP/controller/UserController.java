package edu.dosw.TECHCUP.controller;

import edu.dosw.TECHCUP.controller.dto.UserRequestDTO;
import edu.dosw.TECHCUP.controller.dto.UserResponseDTO;
import edu.dosw.TECHCUP.controller.mapper.UserMapper;
import edu.dosw.TECHCUP.core.model.User;
import edu.dosw.TECHCUP.core.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/usuarios")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    private final UserMapper userMapper = UserMapper.INSTANCE;

    // POST /api/usuarios
    @PostMapping
    public ResponseEntity<UserResponseDTO> crearUsuario(@RequestBody UserRequestDTO request) {
        User user = userService.crearUsuario(request);
        UserResponseDTO response = userMapper.toResponseDTO(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // GET /api/usuarios
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> listarUsuarios() {
        List<UserResponseDTO> response = userService.listarUsuarios()
                .stream()
                .map(userMapper::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    // GET /api/usuarios/{id}
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> obtenerUsuario(@PathVariable Long id) {
        User user = userService.obtenerUsuarioPorId(id);
        UserResponseDTO response = userMapper.toResponseDTO(user);
        return ResponseEntity.ok(response);
    }
}
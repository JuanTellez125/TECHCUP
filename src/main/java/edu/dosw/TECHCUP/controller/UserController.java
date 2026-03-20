package edu.dosw.TECHCUP.controller;

import edu.dosw.TECHCUP.controller.dto.UserRequestDTO;
import edu.dosw.TECHCUP.controller.dto.UserResponseDTO;
import edu.dosw.TECHCUP.controller.mapper.UserMapper;
import edu.dosw.TECHCUP.core.model.User;
import edu.dosw.TECHCUP.core.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/usuarios")
public class UserController {

    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }
    private final UserMapper userMapper = UserMapper.INSTANCE;

    @PostMapping
    public ResponseEntity<UserResponseDTO> crearUsuario(@RequestBody UserRequestDTO request) {
        log.info("POST /api/usuarios - Request to create user");
        User user = userService.crearUsuario(request);
        UserResponseDTO response = userMapper.toResponseDTO(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> listarUsuarios() {
        log.debug("GET /api/usuarios - Listing all users");
        List<UserResponseDTO> response = userService.listarUsuarios()
                .stream()
                .map(userMapper::toResponseDTO)
                .collect(Collectors.toList());
        log.debug("{} users were returned", response.size());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> obtenerUsuario(@PathVariable Long id) {
        log.debug("GET /api/usuarios/{} - Searching for user by ID", id);
        User user = userService.obtenerUsuarioPorId(id);
        UserResponseDTO response = userMapper.toResponseDTO(user);
        return ResponseEntity.ok(response);
    }
}
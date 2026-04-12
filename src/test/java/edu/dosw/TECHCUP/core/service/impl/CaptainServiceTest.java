package edu.dosw.TECHCUP.core.service.impl;

import edu.dosw.TECHCUP.core.exception.UserNotFoundException;
import edu.dosw.TECHCUP.core.exception.UserValidationException;
import edu.dosw.TECHCUP.core.model.User;
import edu.dosw.TECHCUP.core.model.enums.Role;
import edu.dosw.TECHCUP.core.validator.UserValidator;
import edu.dosw.TECHCUP.persistence.entity.UserEntity;
import edu.dosw.TECHCUP.persistence.mapper.UserPersistenceMapper;
import edu.dosw.TECHCUP.persistence.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CaptainServiceTest {

    @Mock UserRepository userRepository;
    @Mock UserPersistenceMapper userPersistenceMapper;
    @Mock UserValidator userValidator;
    @Mock PasswordEncoder passwordEncoder;

    @InjectMocks CaptainService service;

    // ─── createUser ───────────────────────────────────────────────────────────

    @Test
    void createUser_success() {
        User model = buildUserModel();
        UserEntity saved = buildUserEntity(1L);

        when(userRepository.existsByEmail(model.getEmail())).thenReturn(false);
        when(userRepository.existsByDocumentId(model.getDocumentId())).thenReturn(false);
        when(passwordEncoder.encode(any())).thenReturn("encoded");
        when(userRepository.save(any())).thenReturn(saved);
        when(userPersistenceMapper.toModel(saved)).thenReturn(model);

        User result = service.createUser(model);

        assertThat(result).isEqualTo(model);
        verify(userValidator).validate(model);
        verify(userRepository).save(argThat(e -> e.getUserType() == Role.CAPTAIN));
    }

    @Test
    void createUser_emailAlreadyExists_throws() {
        User model = buildUserModel();
        when(userRepository.existsByEmail(model.getEmail())).thenReturn(true);

        assertThatThrownBy(() -> service.createUser(model))
                .isInstanceOf(UserValidationException.class)
                .hasMessageContaining("already exists");
    }

    @Test
    void createUser_documentAlreadyExists_throws() {
        User model = buildUserModel();
        when(userRepository.existsByEmail(model.getEmail())).thenReturn(false);
        when(userRepository.existsByDocumentId(model.getDocumentId())).thenReturn(true);

        assertThatThrownBy(() -> service.createUser(model))
                .isInstanceOf(UserValidationException.class)
                .hasMessageContaining("document");
    }

    // ─── updateUser ───────────────────────────────────────────────────────────

    @Test
    void updateUser_success() {
        User model = buildUserModel();
        UserEntity entity = buildUserEntity(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(userRepository.save(entity)).thenReturn(entity);
        when(userPersistenceMapper.toModel(entity)).thenReturn(model);

        User result = service.updateUser(1L, model);

        assertThat(result).isEqualTo(model);
    }

    @Test
    void updateUser_notFound_throws() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.updateUser(99L, buildUserModel()))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void updateUser_withNewPassword_encodesIt() {
        User model = buildUserModel();
        model.setPassword("newPassword");
        UserEntity entity = buildUserEntity(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(passwordEncoder.encode("newPassword")).thenReturn("encodedNew");
        when(userRepository.save(entity)).thenReturn(entity);
        when(userPersistenceMapper.toModel(entity)).thenReturn(model);

        service.updateUser(1L, model);

        verify(passwordEncoder).encode("newPassword");
    }

    @Test
    void updateUser_withBlankPassword_doesNotEncode() {
        User model = buildUserModel();
        model.setPassword("  ");
        UserEntity entity = buildUserEntity(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(userRepository.save(entity)).thenReturn(entity);
        when(userPersistenceMapper.toModel(entity)).thenReturn(model);

        service.updateUser(1L, model);

        verify(passwordEncoder, never()).encode(any());
    }

    // ─── deleteUser ───────────────────────────────────────────────────────────

    @Test
    void deleteUser_success() {
        when(userRepository.existsById(1L)).thenReturn(true);
        doNothing().when(userRepository).deleteById(1L);

        assertThatCode(() -> service.deleteUser(1L)).doesNotThrowAnyException();
        verify(userRepository).deleteById(1L);
    }

    @Test
    void deleteUser_notFound_throws() {
        when(userRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> service.deleteUser(99L))
                .isInstanceOf(UserNotFoundException.class);
    }

    // ─── getCaptainById ───────────────────────────────────────────────────────

    @Test
    void getCaptainById_found_returnsUser() {
        UserEntity entity = buildUserEntity(1L);
        User model = buildUserModel();

        when(userRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(userPersistenceMapper.toModel(entity)).thenReturn(model);

        User result = service.getCaptainById(1L);

        assertThat(result).isEqualTo(model);
    }

    @Test
    void getCaptainById_notFound_throws() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getCaptainById(99L))
                .isInstanceOf(UserNotFoundException.class);
    }

    // ─── findByEmail ──────────────────────────────────────────────────────────

    @Test
    void findByEmail_found_returnsUser() {
        UserEntity entity = buildUserEntity(1L);
        User model = buildUserModel();

        when(userRepository.findByEmail("juan@gmail.com")).thenReturn(Optional.of(entity));
        when(userPersistenceMapper.toModel(entity)).thenReturn(model);

        Optional<User> result = service.findByEmail("juan@gmail.com");

        assertThat(result).isPresent().contains(model);
    }

    @Test
    void findByEmail_notFound_returnsEmpty() {
        when(userRepository.findByEmail("noexiste@gmail.com")).thenReturn(Optional.empty());

        Optional<User> result = service.findByEmail("noexiste@gmail.com");

        assertThat(result).isEmpty();
    }

    // ─── Helpers ──────────────────────────────────────────────────────────────

    private User buildUserModel() {
        return User.builder()
                .userId(1L)
                .firstName("Juan")
                .lastName("Tellez")
                .email("juan@gmail.com")
                .password("password123")
                .documentId("123456")
                .userType(Role.CAPTAIN)
                .build();
    }

    private UserEntity buildUserEntity(Long id) {
        return UserEntity.builder()
                .user_id(id)
                .firstName("Juan")
                .lastName("Tellez")
                .email("juan@gmail.com")
                .userType(Role.CAPTAIN)
                .active(true)
                .build();
    }
}

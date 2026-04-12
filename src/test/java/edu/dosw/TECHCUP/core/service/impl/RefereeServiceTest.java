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

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RefereeServiceTest {

    @Mock UserRepository userRepository;
    @Mock UserPersistenceMapper userPersistenceMapper;
    @Mock UserValidator userValidator;
    @Mock PasswordEncoder passwordEncoder;

    @InjectMocks RefereeService service;

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
        verify(userRepository).save(argThat(e -> e.getUserType() == Role.REFEREE));
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

    // ─── getRefereeById ───────────────────────────────────────────────────────

    @Test
    void getRefereeById_found_returnsUser() {
        UserEntity entity = buildUserEntity(1L);
        User model = buildUserModel();

        when(userRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(userPersistenceMapper.toModel(entity)).thenReturn(model);

        User result = service.getRefereeById(1L);

        assertThat(result).isEqualTo(model);
    }

    @Test
    void getRefereeById_notFound_throws() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getRefereeById(99L))
                .isInstanceOf(UserNotFoundException.class);
    }

    // ─── getAllReferees ───────────────────────────────────────────────────────

    @Test
    void getAllReferees_returnsMappedList() {
        UserEntity e1 = buildUserEntity(1L);
        UserEntity e2 = buildUserEntity(2L);
        User m1 = buildUserModel();
        User m2 = buildUserModel();

        when(userRepository.findAllByUserType(Role.REFEREE)).thenReturn(List.of(e1, e2));
        when(userPersistenceMapper.toModel(e1)).thenReturn(m1);
        when(userPersistenceMapper.toModel(e2)).thenReturn(m2);

        List<User> result = service.getAllReferees();

        assertThat(result).hasSize(2).contains(m1, m2);
    }

    // ─── findByEmail ──────────────────────────────────────────────────────────

    @Test
    void findByEmail_found_returnsUser() {
        UserEntity entity = buildUserEntity(1L);
        User model = buildUserModel();

        when(userRepository.findByEmail("ref@gmail.com")).thenReturn(Optional.of(entity));
        when(userPersistenceMapper.toModel(entity)).thenReturn(model);

        Optional<User> result = service.findByEmail("ref@gmail.com");

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
                .firstName("Carlos")
                .lastName("Gomez")
                .email("ref@gmail.com")
                .password("password123")
                .documentId("654321")
                .userType(Role.REFEREE)
                .build();
    }

    private UserEntity buildUserEntity(Long id) {
        return UserEntity.builder()
                .user_id(id)
                .firstName("Carlos")
                .lastName("Gomez")
                .email("ref@gmail.com")
                .userType(Role.REFEREE)
                .active(true)
                .build();
    }
}

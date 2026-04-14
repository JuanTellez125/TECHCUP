package edu.dosw.TECHCUP.core.service.impl;

import edu.dosw.TECHCUP.core.exception.InvalidAdminException;
import edu.dosw.TECHCUP.core.exception.UnauthorizedRoleAssignmentException;
import edu.dosw.TECHCUP.core.exception.UserNotFoundException;
import edu.dosw.TECHCUP.core.exception.UserValidationException;
import edu.dosw.TECHCUP.core.model.User;
import edu.dosw.TECHCUP.core.model.enums.Role;
import edu.dosw.TECHCUP.core.validator.UserValidator;
import edu.dosw.TECHCUP.persistence.entity.RoleAuditLogEntity;
import edu.dosw.TECHCUP.persistence.entity.UserEntity;
import edu.dosw.TECHCUP.persistence.mapper.UserPersistenceMapper;
import edu.dosw.TECHCUP.persistence.repository.RoleAuditLogRepository;
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
class AdministratorServiceTest {

    @Mock UserRepository userRepository;
    @Mock UserPersistenceMapper userPersistenceMapper;
    @Mock UserValidator userValidator;
    @Mock PasswordEncoder passwordEncoder;
    @Mock RoleAuditLogRepository roleAuditLogRepository;

    @InjectMocks AdministratorService service;

    // ─── createUser ───────────────────────────────────────────────────────────

    @Test
    void createUser_success() {
        User model = buildUserModel();
        UserEntity saved = buildUserEntity(1L, Role.ADMINISTRATOR);

        when(userRepository.existsByEmail(model.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(any())).thenReturn("encoded");
        when(userRepository.save(any())).thenReturn(saved);
        when(userPersistenceMapper.toModel(saved)).thenReturn(model);

        User result = service.createUser(model);

        assertThat(result).isEqualTo(model);
        verify(userValidator).validate(model);
    }

    @Test
    void createUser_withNullRole_defaultsToAdministrator() {
        User model = buildUserModel();
        model.setUserType(null);
        UserEntity saved = buildUserEntity(1L, Role.ADMINISTRATOR);

        when(userRepository.existsByEmail(model.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(any())).thenReturn("encoded");
        when(userRepository.save(any())).thenReturn(saved);
        when(userPersistenceMapper.toModel(saved)).thenReturn(model);

        service.createUser(model);

        verify(userRepository).save(argThat(e -> e.getUserType() == Role.ADMINISTRATOR));
    }

    @Test
    void createUser_emailAlreadyExists_throws() {
        User model = buildUserModel();
        when(userRepository.existsByEmail(model.getEmail())).thenReturn(true);

        assertThatThrownBy(() -> service.createUser(model))
                .isInstanceOf(UserValidationException.class)
                .hasMessageContaining("already exists");
    }

    // ─── updateUser ───────────────────────────────────────────────────────────

    @Test
    void updateUser_success() {
        User model = buildUserModel();
        UserEntity entity = buildUserEntity(1L, Role.ADMINISTRATOR);

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
        UserEntity entity = buildUserEntity(1L, Role.ADMINISTRATOR);

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

    // ─── findByEmail ──────────────────────────────────────────────────────────

    @Test
    void findByEmail_found_returnsUser() {
        UserEntity entity = buildUserEntity(1L, Role.ADMINISTRATOR);
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

    // ─── assignRole ───────────────────────────────────────────────────────────

    @Test
    void assignRole_success() {
        UserEntity admin = buildUserEntity(1L, Role.ADMINISTRATOR);
        UserEntity target = buildUserEntity(2L, Role.PLAYER);
        User model = buildUserModel();

        when(userRepository.findById(1L)).thenReturn(Optional.of(admin));
        when(userRepository.findById(2L)).thenReturn(Optional.of(target));
        when(userRepository.save(target)).thenReturn(target);
        when(userPersistenceMapper.toModel(target)).thenReturn(model);
        when(roleAuditLogRepository.save(any(RoleAuditLogEntity.class))).thenAnswer(i -> i.getArgument(0));

        User result = service.assignRole(1L, 2L, Role.CAPTAIN);

        assertThat(result).isEqualTo(model);
        assertThat(target.getUserType()).isEqualTo(Role.CAPTAIN);
        verify(roleAuditLogRepository).save(argThat(log ->
                log.getAdminId().equals(1L) &&
                log.getTargetUserId().equals(2L) &&
                log.getPreviousRole() == Role.PLAYER &&
                log.getNewRole() == Role.CAPTAIN
        ));
    }

    @Test
    void assignRole_notAdmin_throws() {
        UserEntity notAdmin = buildUserEntity(1L, Role.PLAYER);
        when(userRepository.findById(1L)).thenReturn(Optional.of(notAdmin));

        assertThatThrownBy(() -> service.assignRole(1L, 2L, Role.CAPTAIN))
                .isInstanceOf(InvalidAdminException.class)
                .hasMessageContaining("1");
    }

    @Test
    void assignRole_adminNotFound_throws() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.assignRole(99L, 2L, Role.CAPTAIN))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void assignRole_targetNotFound_throws() {
        UserEntity admin = buildUserEntity(1L, Role.ADMINISTRATOR);
        when(userRepository.findById(1L)).thenReturn(Optional.of(admin));
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.assignRole(1L, 99L, Role.CAPTAIN))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void assignRole_newRoleIsAdministrator_throws() {
        UserEntity admin = buildUserEntity(1L, Role.ADMINISTRATOR);
        when(userRepository.findById(1L)).thenReturn(Optional.of(admin));

        assertThatThrownBy(() -> service.assignRole(1L, 2L, Role.ADMINISTRATOR))
                .isInstanceOf(UnauthorizedRoleAssignmentException.class);
    }

    @Test
    void assignRole_auditLogPersisted_withCorrectTimestamp() {
        UserEntity admin = buildUserEntity(1L, Role.ADMINISTRATOR);
        UserEntity target = buildUserEntity(2L, Role.REFEREE);
        User model = buildUserModel();

        when(userRepository.findById(1L)).thenReturn(Optional.of(admin));
        when(userRepository.findById(2L)).thenReturn(Optional.of(target));
        when(userRepository.save(target)).thenReturn(target);
        when(userPersistenceMapper.toModel(target)).thenReturn(model);
        when(roleAuditLogRepository.save(any(RoleAuditLogEntity.class))).thenAnswer(i -> i.getArgument(0));

        service.assignRole(1L, 2L, Role.ORGANIZER);

        verify(roleAuditLogRepository).save(argThat(log ->
                log.getTimestamp() != null &&
                log.getPreviousRole() == Role.REFEREE &&
                log.getNewRole() == Role.ORGANIZER
        ));
    }

    // ─── getAllUsers / getAdminById ────────────────────────────────────────────

    @Test
    void getAllUsers_returnsMappedList() {
        UserEntity e1 = buildUserEntity(1L, Role.ADMINISTRATOR);
        UserEntity e2 = buildUserEntity(2L, Role.PLAYER);
        User m1 = buildUserModel();
        User m2 = buildUserModel();

        when(userRepository.findAll()).thenReturn(List.of(e1, e2));
        when(userPersistenceMapper.toModel(e1)).thenReturn(m1);
        when(userPersistenceMapper.toModel(e2)).thenReturn(m2);

        List<User> result = service.getAllUsers();

        assertThat(result).hasSize(2).contains(m1, m2);
    }

    @Test
    void getAdminById_found_returnsUser() {
        UserEntity entity = buildUserEntity(1L, Role.ADMINISTRATOR);
        User model = buildUserModel();

        when(userRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(userPersistenceMapper.toModel(entity)).thenReturn(model);

        User result = service.getAdminById(1L);

        assertThat(result).isEqualTo(model);
    }

    @Test
    void getAdminById_notFound_throws() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getAdminById(99L))
                .isInstanceOf(UserNotFoundException.class);
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
                .userType(Role.ADMINISTRATOR)
                .build();
    }

    private UserEntity buildUserEntity(Long id, Role role) {
        return UserEntity.builder()
                .user_id(id)
                .firstName("Juan")
                .lastName("Tellez")
                .email("juan@gmail.com")
                .userType(role)
                .active(true)
                .build();
    }
}

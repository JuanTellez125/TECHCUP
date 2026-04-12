package edu.dosw.TECHCUP.core.service.impl;

import edu.dosw.TECHCUP.controller.dto.request.SportProfileRequestDTO;
import edu.dosw.TECHCUP.controller.dto.response.SportProfileResponseDTO;
import edu.dosw.TECHCUP.controller.mapper.SportProfileMapper;
import edu.dosw.TECHCUP.core.exception.SportProfileException;
import edu.dosw.TECHCUP.core.exception.UserNotFoundException;
import edu.dosw.TECHCUP.core.exception.UserValidationException;
import edu.dosw.TECHCUP.core.model.SportProfile;
import edu.dosw.TECHCUP.core.model.User;
import edu.dosw.TECHCUP.core.model.enums.Position;
import edu.dosw.TECHCUP.core.model.enums.Role;
import edu.dosw.TECHCUP.core.validator.UserValidator;
import edu.dosw.TECHCUP.persistence.entity.SportProfileEntity;
import edu.dosw.TECHCUP.persistence.entity.UserEntity;
import edu.dosw.TECHCUP.persistence.mapper.SportProfilePersistenceMapper;
import edu.dosw.TECHCUP.persistence.mapper.UserPersistenceMapper;
import edu.dosw.TECHCUP.persistence.repository.SportProfileRepository;
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
class PlayerServiceTest {

    @Mock UserRepository userRepository;
    @Mock UserPersistenceMapper userPersistenceMapper;
    @Mock SportProfileRepository sportProfileRepository;
    @Mock SportProfileMapper sportProfileMapper;
    @Mock SportProfilePersistenceMapper sportProfilePersistenceMapper;
    @Mock UserValidator userValidator;
    @Mock PasswordEncoder passwordEncoder;

    @InjectMocks PlayerService service;

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
        verify(userRepository).save(argThat(e -> e.getUserType() == Role.PLAYER));
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
        model.setPassword("newPass");
        UserEntity entity = buildUserEntity(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(passwordEncoder.encode("newPass")).thenReturn("encodedNew");
        when(userRepository.save(entity)).thenReturn(entity);
        when(userPersistenceMapper.toModel(entity)).thenReturn(model);

        service.updateUser(1L, model);

        verify(passwordEncoder).encode("newPass");
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

    // ─── getPlayerById / getAllPlayers ────────────────────────────────────────

    @Test
    void getPlayerById_found_returnsUser() {
        UserEntity entity = buildUserEntity(1L);
        User model = buildUserModel();

        when(userRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(userPersistenceMapper.toModel(entity)).thenReturn(model);

        User result = service.getPlayerById(1L);

        assertThat(result).isEqualTo(model);
    }

    @Test
    void getPlayerById_notFound_throws() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getPlayerById(99L))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void getAllPlayers_returnsMappedList() {
        UserEntity e1 = buildUserEntity(1L);
        UserEntity e2 = buildUserEntity(2L);
        User m1 = buildUserModel();
        User m2 = buildUserModel();

        when(userRepository.findAllByUserType(Role.PLAYER)).thenReturn(List.of(e1, e2));
        when(userPersistenceMapper.toModel(e1)).thenReturn(m1);
        when(userPersistenceMapper.toModel(e2)).thenReturn(m2);

        List<User> result = service.getAllPlayers();

        assertThat(result).hasSize(2);
    }

    // ─── setAvailability ──────────────────────────────────────────────────────

    @Test
    void setAvailability_found_updatesAndReturns() {
        SportProfileEntity profile = buildProfileEntity(1L);
        SportProfile model = new SportProfile();
        SportProfileResponseDTO dto = new SportProfileResponseDTO();

        when(sportProfileRepository.findByUser_User_id(1L)).thenReturn(Optional.of(profile));
        when(sportProfileRepository.save(profile)).thenReturn(profile);
        when(sportProfilePersistenceMapper.toModel(profile)).thenReturn(model);
        when(sportProfileMapper.toDto(model)).thenReturn(dto);

        SportProfileResponseDTO result = service.setAvailability(1L, false);

        assertThat(result).isEqualTo(dto);
        assertThat(profile.isAvailable()).isFalse();
    }

    @Test
    void setAvailability_notFound_throws() {
        when(sportProfileRepository.findByUser_User_id(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.setAvailability(99L, true))
                .isInstanceOf(SportProfileException.class);
    }

    // ─── getSportProfile ──────────────────────────────────────────────────────

    @Test
    void getSportProfile_found_returnsDto() {
        SportProfileEntity profile = buildProfileEntity(1L);
        SportProfile model = new SportProfile();
        SportProfileResponseDTO dto = new SportProfileResponseDTO();

        when(sportProfileRepository.findByUser_User_id(1L)).thenReturn(Optional.of(profile));
        when(sportProfilePersistenceMapper.toModel(profile)).thenReturn(model);
        when(sportProfileMapper.toDto(model)).thenReturn(dto);

        SportProfileResponseDTO result = service.getSportProfile(1L);

        assertThat(result).isEqualTo(dto);
    }

    @Test
    void getSportProfile_notFound_throws() {
        when(sportProfileRepository.findByUser_User_id(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getSportProfile(99L))
                .isInstanceOf(SportProfileException.class);
    }

    // ─── saveSportProfile ─────────────────────────────────────────────────────

    @Test
    void saveSportProfile_createsNewProfile() {
        UserEntity user = buildUserEntity(1L);
        SportProfileRequestDTO dto = SportProfileRequestDTO.builder()
                .primaryPosition(Position.PORTERO)
                .available(true)
                .build();
        SportProfileEntity saved = buildProfileEntity(1L);
        SportProfile model = new SportProfile();
        SportProfileResponseDTO responseDTO = new SportProfileResponseDTO();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(sportProfileRepository.findByUser_User_id(1L)).thenReturn(Optional.empty());
        when(sportProfileRepository.save(any())).thenReturn(saved);
        when(sportProfilePersistenceMapper.toModel(saved)).thenReturn(model);
        when(sportProfileMapper.toDto(model)).thenReturn(responseDTO);

        SportProfileResponseDTO result = service.saveSportProfile(1L, dto);

        assertThat(result).isEqualTo(responseDTO);
    }

    @Test
    void saveSportProfile_updatesExistingProfile() {
        UserEntity user = buildUserEntity(1L);
        SportProfileEntity existing = buildProfileEntity(1L);
        SportProfileRequestDTO dto = SportProfileRequestDTO.builder()
                .primaryPosition(Position.DELANTERO)
                .available(false)
                .build();
        SportProfile model = new SportProfile();
        SportProfileResponseDTO responseDTO = new SportProfileResponseDTO();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(sportProfileRepository.findByUser_User_id(1L)).thenReturn(Optional.of(existing));
        when(sportProfileRepository.save(existing)).thenReturn(existing);
        when(sportProfilePersistenceMapper.toModel(existing)).thenReturn(model);
        when(sportProfileMapper.toDto(model)).thenReturn(responseDTO);

        SportProfileResponseDTO result = service.saveSportProfile(1L, dto);

        assertThat(result).isEqualTo(responseDTO);
        assertThat(existing.getPrimaryPosition()).isEqualTo(Position.DELANTERO);
    }

    @Test
    void saveSportProfile_userNotFound_throws() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.saveSportProfile(99L,
                SportProfileRequestDTO.builder().primaryPosition(Position.PORTERO).available(true).build()))
                .isInstanceOf(UserNotFoundException.class);
    }

    // ─── findByEmail ──────────────────────────────────────────────────────────

    @Test
    void findByEmail_found_returnsUser() {
        UserEntity entity = buildUserEntity(1L);
        User model = buildUserModel();

        when(userRepository.findByEmail("player@gmail.com")).thenReturn(Optional.of(entity));
        when(userPersistenceMapper.toModel(entity)).thenReturn(model);

        Optional<User> result = service.findByEmail("player@gmail.com");

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
                .firstName("Luis")
                .lastName("Rodriguez")
                .email("player@gmail.com")
                .password("password123")
                .documentId("111222")
                .userType(Role.PLAYER)
                .build();
    }

    private UserEntity buildUserEntity(Long id) {
        return UserEntity.builder()
                .user_id(id)
                .firstName("Luis")
                .lastName("Rodriguez")
                .email("player@gmail.com")
                .userType(Role.PLAYER)
                .active(true)
                .build();
    }

    private SportProfileEntity buildProfileEntity(Long userId) {
        UserEntity user = buildUserEntity(userId);
        return SportProfileEntity.builder()
                .user(user)
                .primaryPosition(Position.PORTERO)
                .available(true)
                .build();
    }
}

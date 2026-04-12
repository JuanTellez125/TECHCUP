package edu.dosw.TECHCUP.core.service;

import edu.dosw.TECHCUP.controller.dto.request.TeamRequestDTO;
import edu.dosw.TECHCUP.controller.dto.response.InvitationResponseDTO;
import edu.dosw.TECHCUP.controller.dto.response.SportProfileResponseDTO;
import edu.dosw.TECHCUP.controller.dto.response.TeamResponseDTO;
import edu.dosw.TECHCUP.controller.mapper.InvitationMapper;
import edu.dosw.TECHCUP.controller.mapper.SportProfileMapper;
import edu.dosw.TECHCUP.controller.mapper.TeamMapper;
import edu.dosw.TECHCUP.core.exception.UserNotFoundException;
import edu.dosw.TECHCUP.core.exception.UserValidationException;
import edu.dosw.TECHCUP.core.model.Invitation;
import edu.dosw.TECHCUP.core.model.SportProfile;
import edu.dosw.TECHCUP.core.model.Team;
import edu.dosw.TECHCUP.core.model.enums.InvitationStatus;
import edu.dosw.TECHCUP.core.model.enums.Role;
import edu.dosw.TECHCUP.core.model.enums.TeamMemberStatus;
import edu.dosw.TECHCUP.core.validator.TeamValidator;
import edu.dosw.TECHCUP.persistence.entity.*;
import edu.dosw.TECHCUP.persistence.mapper.InvitationPersistenceMapper;
import edu.dosw.TECHCUP.persistence.mapper.SportProfilePersistenceMapper;
import edu.dosw.TECHCUP.persistence.mapper.TeamPersistenceMapper;
import edu.dosw.TECHCUP.persistence.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TeamServiceTest {

    @Mock TeamRepository teamRepository;
    @Mock UserRepository userRepository;
    @Mock InvitationRepository invitationRepository;
    @Mock TeamMemberRepository teamMemberRepository;
    @Mock SportProfileRepository sportProfileRepository;
    @Mock TournamentRegistrationRepository tournamentRegistrationRepository;
    @Mock TeamMapper teamMapper;
    @Mock InvitationMapper invitationMapper;
    @Mock SportProfileMapper sportProfileMapper;
    @Mock TeamValidator teamValidator;
    @Mock TeamPersistenceMapper teamPersistenceMapper;
    @Mock InvitationPersistenceMapper invitationPersistenceMapper;
    @Mock SportProfilePersistenceMapper sportProfilePersistenceMapper;

    @InjectMocks TeamService service;

    // ─── createTeam ───────────────────────────────────────────────────────────

    @Test
    void createTeam_success() {
        UserEntity captain = buildUserEntity(1L, Role.CAPTAIN);
        TeamEntity saved = buildTeamEntity(10L, captain);
        Team model = new Team();
        TeamResponseDTO dto = new TeamResponseDTO();
        TeamRequestDTO request = TeamRequestDTO.builder().name("Los Campeones").build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(captain));
        when(teamRepository.existsByName("Los Campeones")).thenReturn(false);
        when(teamMemberRepository.existsByTeam_IdAndUser_User_id(null, 1L)).thenReturn(false);
        when(teamRepository.save(any())).thenReturn(saved);
        when(teamPersistenceMapper.toModel(saved)).thenReturn(model);
        when(teamMapper.toDto(model)).thenReturn(dto);

        TeamResponseDTO result = service.createTeam(1L, request);

        assertThat(result).isEqualTo(dto);
        verify(teamValidator).validateCaptainRole(captain);
        verify(teamValidator).validateTeamName("Los Campeones");
    }

    @Test
    void createTeam_captainNotFound_throws() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.createTeam(99L, TeamRequestDTO.builder().name("Team").build()))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void createTeam_nameAlreadyExists_throws() {
        UserEntity captain = buildUserEntity(1L, Role.CAPTAIN);
        when(userRepository.findById(1L)).thenReturn(Optional.of(captain));
        when(teamRepository.existsByName("Existing")).thenReturn(true);

        assertThatThrownBy(() -> service.createTeam(1L, TeamRequestDTO.builder().name("Existing").build()))
                .isInstanceOf(UserValidationException.class)
                .hasMessageContaining("name");
    }

    @Test
    void createTeam_captainAlreadyInTeam_throws() {
        UserEntity captain = buildUserEntity(1L, Role.CAPTAIN);
        when(userRepository.findById(1L)).thenReturn(Optional.of(captain));
        when(teamRepository.existsByName("New Team")).thenReturn(false);
        when(teamMemberRepository.existsByTeam_IdAndUser_User_id(null, 1L)).thenReturn(true);

        assertThatThrownBy(() -> service.createTeam(1L, TeamRequestDTO.builder().name("New Team").build()))
                .isInstanceOf(UserValidationException.class)
                .hasMessageContaining("captain");
    }

    // ─── getTeamById ──────────────────────────────────────────────────────────

    @Test
    void getTeamById_found_returnsDto() {
        UserEntity captain = buildUserEntity(1L, Role.CAPTAIN);
        TeamEntity team = buildTeamEntity(10L, captain);
        Team model = new Team();
        TeamResponseDTO dto = new TeamResponseDTO();

        when(teamRepository.findById(10L)).thenReturn(Optional.of(team));
        when(teamPersistenceMapper.toModel(team)).thenReturn(model);
        when(teamMapper.toDto(model)).thenReturn(dto);

        TeamResponseDTO result = service.getTeamById(10L);

        assertThat(result).isEqualTo(dto);
    }

    @Test
    void getTeamById_notFound_throws() {
        when(teamRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getTeamById(99L))
                .isInstanceOf(UserNotFoundException.class);
    }

    // ─── invitePlayer ─────────────────────────────────────────────────────────

    @Test
    void invitePlayer_success() {
        UserEntity captain = buildUserEntity(1L, Role.CAPTAIN);
        UserEntity player = buildUserEntity(2L, Role.PLAYER);
        TeamEntity team = buildTeamEntity(10L, captain);
        InvitationEntity saved = buildInvitationEntity(100L, team, player, captain);
        Invitation model = new Invitation();
        InvitationResponseDTO dto = new InvitationResponseDTO();

        when(userRepository.findById(1L)).thenReturn(Optional.of(captain));
        when(teamRepository.findById(10L)).thenReturn(Optional.of(team));
        when(userRepository.findById(2L)).thenReturn(Optional.of(player));
        when(teamMemberRepository.existsByTeam_IdAndUser_User_id(10L, 2L)).thenReturn(false);
        when(teamMemberRepository.findAllByTeam_Id(10L)).thenReturn(List.of());
        when(invitationRepository.save(any())).thenReturn(saved);
        when(invitationPersistenceMapper.toModel(saved)).thenReturn(model);
        when(invitationMapper.toDto(model)).thenReturn(dto);

        InvitationResponseDTO result = service.invitePlayer(1L, 10L, 2L);

        assertThat(result).isEqualTo(dto);
    }

    @Test
    void invitePlayer_notCaptainOfTeam_throws() {
        UserEntity captain = buildUserEntity(1L, Role.CAPTAIN);
        UserEntity otherCaptain = buildUserEntity(99L, Role.CAPTAIN);
        TeamEntity team = buildTeamEntity(10L, otherCaptain);

        when(userRepository.findById(1L)).thenReturn(Optional.of(captain));
        when(teamRepository.findById(10L)).thenReturn(Optional.of(team));

        assertThatThrownBy(() -> service.invitePlayer(1L, 10L, 2L))
                .isInstanceOf(UserValidationException.class)
                .hasMessageContaining("captain");
    }

    @Test
    void invitePlayer_playerAlreadyInTeam_throws() {
        UserEntity captain = buildUserEntity(1L, Role.CAPTAIN);
        UserEntity player = buildUserEntity(2L, Role.PLAYER);
        TeamEntity team = buildTeamEntity(10L, captain);

        when(userRepository.findById(1L)).thenReturn(Optional.of(captain));
        when(teamRepository.findById(10L)).thenReturn(Optional.of(team));
        when(userRepository.findById(2L)).thenReturn(Optional.of(player));
        when(teamMemberRepository.existsByTeam_IdAndUser_User_id(10L, 2L)).thenReturn(true);

        assertThatThrownBy(() -> service.invitePlayer(1L, 10L, 2L))
                .isInstanceOf(UserValidationException.class)
                .hasMessageContaining("already belongs");
    }

    // ─── respondInvitation ────────────────────────────────────────────────────

    @Test
    void respondInvitation_accept_createsTeamMember() {
        UserEntity captain = buildUserEntity(1L, Role.CAPTAIN);
        UserEntity player = buildUserEntity(2L, Role.PLAYER);
        TeamEntity team = buildTeamEntity(10L, captain);
        InvitationEntity invitation = buildInvitationEntity(100L, team, player, captain);
        invitation.setStatus(InvitationStatus.PENDIENTE);
        Invitation model = new Invitation();
        InvitationResponseDTO dto = new InvitationResponseDTO();

        when(invitationRepository.findById(100L)).thenReturn(Optional.of(invitation));
        when(invitationRepository.save(invitation)).thenReturn(invitation);
        when(invitationPersistenceMapper.toModel(invitation)).thenReturn(model);
        when(invitationMapper.toDto(model)).thenReturn(dto);

        InvitationResponseDTO result = service.respondInvitation(2L, 100L, true);

        assertThat(result).isEqualTo(dto);
        assertThat(invitation.getStatus()).isEqualTo(InvitationStatus.ACEPTADA);
        verify(teamMemberRepository).save(any());
    }

    @Test
    void respondInvitation_reject_updatesStatus() {
        UserEntity captain = buildUserEntity(1L, Role.CAPTAIN);
        UserEntity player = buildUserEntity(2L, Role.PLAYER);
        TeamEntity team = buildTeamEntity(10L, captain);
        InvitationEntity invitation = buildInvitationEntity(100L, team, player, captain);
        invitation.setStatus(InvitationStatus.PENDIENTE);
        Invitation model = new Invitation();
        InvitationResponseDTO dto = new InvitationResponseDTO();

        when(invitationRepository.findById(100L)).thenReturn(Optional.of(invitation));
        when(invitationRepository.save(invitation)).thenReturn(invitation);
        when(invitationPersistenceMapper.toModel(invitation)).thenReturn(model);
        when(invitationMapper.toDto(model)).thenReturn(dto);

        InvitationResponseDTO result = service.respondInvitation(2L, 100L, false);

        assertThat(result).isEqualTo(dto);
        assertThat(invitation.getStatus()).isEqualTo(InvitationStatus.RECHAZADA);
    }

    @Test
    void respondInvitation_notForThisPlayer_throws() {
        UserEntity captain = buildUserEntity(1L, Role.CAPTAIN);
        UserEntity player = buildUserEntity(2L, Role.PLAYER);
        TeamEntity team = buildTeamEntity(10L, captain);
        InvitationEntity invitation = buildInvitationEntity(100L, team, player, captain);

        when(invitationRepository.findById(100L)).thenReturn(Optional.of(invitation));

        assertThatThrownBy(() -> service.respondInvitation(99L, 100L, true))
                .isInstanceOf(UserValidationException.class)
                .hasMessageContaining("not for you");
    }

    @Test
    void respondInvitation_alreadyAnswered_throws() {
        UserEntity captain = buildUserEntity(1L, Role.CAPTAIN);
        UserEntity player = buildUserEntity(2L, Role.PLAYER);
        TeamEntity team = buildTeamEntity(10L, captain);
        InvitationEntity invitation = buildInvitationEntity(100L, team, player, captain);
        invitation.setStatus(InvitationStatus.ACEPTADA);

        when(invitationRepository.findById(100L)).thenReturn(Optional.of(invitation));

        assertThatThrownBy(() -> service.respondInvitation(2L, 100L, true))
                .isInstanceOf(UserValidationException.class)
                .hasMessageContaining("already been answered");
    }

    // ─── getInvitationsByPlayer / getTeamsByTournament ────────────────────────

    @Test
    void getInvitationsByPlayer_returnsMappedList() {
        UserEntity captain = buildUserEntity(1L, Role.CAPTAIN);
        UserEntity player = buildUserEntity(2L, Role.PLAYER);
        TeamEntity team = buildTeamEntity(10L, captain);
        InvitationEntity inv = buildInvitationEntity(100L, team, player, captain);
        Invitation model = new Invitation();
        InvitationResponseDTO dto = new InvitationResponseDTO();

        when(invitationRepository.findAllByInvitedUser_User_id(2L)).thenReturn(List.of(inv));
        when(invitationPersistenceMapper.toModel(inv)).thenReturn(model);
        when(invitationMapper.toDto(model)).thenReturn(dto);

        List<InvitationResponseDTO> result = service.getInvitationsByPlayer(2L);

        assertThat(result).hasSize(1).contains(dto);
    }

    // ─── Helpers ──────────────────────────────────────────────────────────────

    private UserEntity buildUserEntity(Long id, Role role) {
        return UserEntity.builder()
                .user_id(id)
                .firstName("Test")
                .lastName("User")
                .email("test" + id + "@gmail.com")
                .userType(role)
                .active(true)
                .build();
    }

    private TeamEntity buildTeamEntity(Long id, UserEntity captain) {
        return TeamEntity.builder()
                .id(id)
                .name("Los Campeones")
                .captain(captain)
                .active(true)
                .build();
    }

    private InvitationEntity buildInvitationEntity(Long id, TeamEntity team,
                                                    UserEntity invited, UserEntity invitedBy) {
        return InvitationEntity.builder()
                .invitation_id(id)
                .team(team)
                .invitedUser(invited)
                .invitedBy(invitedBy)
                .status(InvitationStatus.PENDIENTE)
                .build();
    }
}

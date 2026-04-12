package edu.dosw.TECHCUP.core.service;

import edu.dosw.TECHCUP.controller.dto.request.LineUpRequestDTO;
import edu.dosw.TECHCUP.controller.dto.response.LineUpResponseDTO;
import edu.dosw.TECHCUP.controller.mapper.LineUpMapper;
import edu.dosw.TECHCUP.core.exception.TeamNotFoundException;
import edu.dosw.TECHCUP.core.exception.UserNotFoundException;
import edu.dosw.TECHCUP.core.exception.UserValidationException;
import edu.dosw.TECHCUP.core.model.LineUp;
import edu.dosw.TECHCUP.core.model.enums.LineUpRole;
import edu.dosw.TECHCUP.core.model.enums.Role;
import edu.dosw.TECHCUP.persistence.entity.*;
import edu.dosw.TECHCUP.persistence.mapper.LineUpPersistenceMapper;
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
class LineUpServiceTest {

    @Mock LineUpRepository lineUpRepository;
    @Mock LineUpMapper lineUpMapper;
    @Mock LineUpPersistenceMapper lineUpPersistenceMapper;
    @Mock TeamRepository teamRepository;
    @Mock MatchRepository matchRepository;
    @Mock TeamMemberRepository teamMemberRepository;
    @Mock UserRepository userRepository;

    @InjectMocks LineUpService service;

    // ─── saveLineUp ───────────────────────────────────────────────────────────

    @Test
    void saveLineUp_success() {
        UserEntity captain = buildUserEntity(1L, Role.CAPTAIN);
        MatchEntity match = buildMatchEntity(5L);
        TeamEntity team = buildTeamEntity(10L, captain);
        UserEntity player = buildUserEntity(2L, Role.PLAYER);
        LineUpEntity saved = buildLineUpEntity(100L, match, team, player);
        LineUp model = new LineUp();
        LineUpResponseDTO dto = new LineUpResponseDTO();
        LineUpRequestDTO request = buildRequest(5L, 10L, 2L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(captain));
        when(matchRepository.findById(5L)).thenReturn(Optional.of(match));
        when(teamRepository.findById(10L)).thenReturn(Optional.of(team));
        when(userRepository.findById(2L)).thenReturn(Optional.of(player));
        when(teamMemberRepository.existsByTeam_IdAndUser_User_id(10L, 2L)).thenReturn(true);
        when(lineUpRepository.findAllByMatch_Match_idAndTeam_Id(5L, 10L)).thenReturn(List.of());
        when(lineUpRepository.save(any())).thenReturn(saved);
        when(lineUpPersistenceMapper.toModel(saved)).thenReturn(model);
        when(lineUpMapper.toDto(model)).thenReturn(dto);

        LineUpResponseDTO result = service.saveLineUp(1L, request);

        assertThat(result).isEqualTo(dto);
    }

    @Test
    void saveLineUp_notCaptain_throws() {
        UserEntity player = buildUserEntity(1L, Role.PLAYER);
        when(userRepository.findById(1L)).thenReturn(Optional.of(player));

        assertThatThrownBy(() -> service.saveLineUp(1L, buildRequest(5L, 10L, 2L)))
                .isInstanceOf(UserValidationException.class)
                .hasMessageContaining("captain");
    }

    @Test
    void saveLineUp_captainNotFound_throws() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.saveLineUp(99L, buildRequest(5L, 10L, 2L)))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void saveLineUp_matchNotFound_throws() {
        UserEntity captain = buildUserEntity(1L, Role.CAPTAIN);
        when(userRepository.findById(1L)).thenReturn(Optional.of(captain));
        when(matchRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.saveLineUp(1L, buildRequest(99L, 10L, 2L)))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void saveLineUp_notCaptainOfTeam_throws() {
        UserEntity captain = buildUserEntity(1L, Role.CAPTAIN);
        UserEntity otherCaptain = buildUserEntity(99L, Role.CAPTAIN);
        MatchEntity match = buildMatchEntity(5L);
        TeamEntity team = buildTeamEntity(10L, otherCaptain);

        when(userRepository.findById(1L)).thenReturn(Optional.of(captain));
        when(matchRepository.findById(5L)).thenReturn(Optional.of(match));
        when(teamRepository.findById(10L)).thenReturn(Optional.of(team));

        assertThatThrownBy(() -> service.saveLineUp(1L, buildRequest(5L, 10L, 2L)))
                .isInstanceOf(UserValidationException.class)
                .hasMessageContaining("captain of this team");
    }

    @Test
    void saveLineUp_playerNotInTeam_throws() {
        UserEntity captain = buildUserEntity(1L, Role.CAPTAIN);
        MatchEntity match = buildMatchEntity(5L);
        TeamEntity team = buildTeamEntity(10L, captain);
        UserEntity player = buildUserEntity(2L, Role.PLAYER);

        when(userRepository.findById(1L)).thenReturn(Optional.of(captain));
        when(matchRepository.findById(5L)).thenReturn(Optional.of(match));
        when(teamRepository.findById(10L)).thenReturn(Optional.of(team));
        when(userRepository.findById(2L)).thenReturn(Optional.of(player));
        when(teamMemberRepository.existsByTeam_IdAndUser_User_id(10L, 2L)).thenReturn(false);

        assertThatThrownBy(() -> service.saveLineUp(1L, buildRequest(5L, 10L, 2L)))
                .isInstanceOf(UserValidationException.class)
                .hasMessageContaining("not belong");
    }

    @Test
    void saveLineUp_playerAlreadyInLineUp_throws() {
        UserEntity captain = buildUserEntity(1L, Role.CAPTAIN);
        MatchEntity match = buildMatchEntity(5L);
        TeamEntity team = buildTeamEntity(10L, captain);
        UserEntity player = buildUserEntity(2L, Role.PLAYER);
        LineUpEntity existing = buildLineUpEntity(100L, match, team, player);

        when(userRepository.findById(1L)).thenReturn(Optional.of(captain));
        when(matchRepository.findById(5L)).thenReturn(Optional.of(match));
        when(teamRepository.findById(10L)).thenReturn(Optional.of(team));
        when(userRepository.findById(2L)).thenReturn(Optional.of(player));
        when(teamMemberRepository.existsByTeam_IdAndUser_User_id(10L, 2L)).thenReturn(true);
        when(lineUpRepository.findAllByMatch_Match_idAndTeam_Id(5L, 10L)).thenReturn(List.of(existing));

        assertThatThrownBy(() -> service.saveLineUp(1L, buildRequest(5L, 10L, 2L)))
                .isInstanceOf(UserValidationException.class)
                .hasMessageContaining("already registered");
    }

    // ─── getLineUp ────────────────────────────────────────────────────────────

    @Test
    void getLineUp_found_returnsDto() {
        UserEntity captain = buildUserEntity(1L, Role.CAPTAIN);
        MatchEntity match = buildMatchEntity(5L);
        TeamEntity team = buildTeamEntity(10L, captain);
        UserEntity player = buildUserEntity(2L, Role.PLAYER);
        LineUpEntity entity = buildLineUpEntity(100L, match, team, player);
        LineUp model = new LineUp();
        LineUpResponseDTO dto = new LineUpResponseDTO();

        when(lineUpRepository.findByMatchAndTeam(10L, 5L)).thenReturn(Optional.of(entity));
        when(lineUpPersistenceMapper.toModel(entity)).thenReturn(model);
        when(lineUpMapper.toDto(model)).thenReturn(dto);

        LineUpResponseDTO result = service.getLineUp(10L, 5L);

        assertThat(result).isEqualTo(dto);
    }

    @Test
    void getLineUp_notFound_throws() {
        when(lineUpRepository.findByMatchAndTeam(99L, 99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getLineUp(99L, 99L))
                .isInstanceOf(TeamNotFoundException.class);
    }

    // ─── Helpers ──────────────────────────────────────────────────────────────

    private LineUpRequestDTO buildRequest(Long matchId, Long teamId, Long userId) {
        return LineUpRequestDTO.builder()
                .matchId(matchId)
                .teamId(teamId)
                .userId(userId)
                .role(LineUpRole.TITULAR)
                .position("Portero")
                .jerseyNumber(1)
                .build();
    }

    private UserEntity buildUserEntity(Long id, Role role) {
        return UserEntity.builder()
                .user_id(id)
                .firstName("Test")
                .lastName("User")
                .email("user" + id + "@gmail.com")
                .userType(role)
                .active(true)
                .build();
    }

    private MatchEntity buildMatchEntity(Long id) {
        return MatchEntity.builder()
                .match_id(id)
                .status("PROGRAMADO")
                .build();
    }

    private TeamEntity buildTeamEntity(Long id, UserEntity captain) {
        return TeamEntity.builder()
                .id(id)
                .name("Team " + id)
                .captain(captain)
                .active(true)
                .build();
    }

    private LineUpEntity buildLineUpEntity(Long id, MatchEntity match, TeamEntity team, UserEntity user) {
        return LineUpEntity.builder()
                .lineUp_id(id)
                .match(match)
                .team(team)
                .user(user)
                .role(LineUpRole.TITULAR)
                .position("Portero")
                .jerseyNumber(1)
                .build();
    }
}

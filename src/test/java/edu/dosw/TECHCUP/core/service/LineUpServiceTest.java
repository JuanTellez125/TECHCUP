package edu.dosw.TECHCUP.core.service;

import edu.dosw.TECHCUP.controller.dto.request.LineUpEntryDTO;
import edu.dosw.TECHCUP.controller.dto.request.LineUpRequestDTO;
import edu.dosw.TECHCUP.controller.dto.response.LineUpResponseDTO;
import edu.dosw.TECHCUP.controller.mapper.LineUpMapper;
import edu.dosw.TECHCUP.core.exception.UserNotFoundException;
import edu.dosw.TECHCUP.core.exception.UserValidationException;
import edu.dosw.TECHCUP.core.model.LineUp;
import edu.dosw.TECHCUP.core.model.enums.LineUpRole;
import edu.dosw.TECHCUP.core.model.enums.Role;
import edu.dosw.TECHCUP.core.model.enums.TeamMemberStatus;
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
        TeamEntity team = buildTeamEntity(10L, captain);
        MatchEntity match = buildMatchEntity(5L, team, buildTeamEntity(11L, captain));
        UserEntity player = buildUserEntity(2L, Role.PLAYER);
        TeamMemberEntity member = TeamMemberEntity.builder().status(TeamMemberStatus.ACEPTADO).build();
        LineUpEntity saved = buildLineUpEntity(100L, match, team, player);
        LineUp model = new LineUp();
        LineUpResponseDTO dto = new LineUpResponseDTO();
        LineUpRequestDTO request = buildRequest(10L, 2L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(captain));
        when(matchRepository.findById(5L)).thenReturn(Optional.of(match));
        when(teamRepository.findById(10L)).thenReturn(Optional.of(team));
        when(userRepository.findById(2L)).thenReturn(Optional.of(player));
        when(teamMemberRepository.findByTeam_IdAndUser_User_id(10L, 2L)).thenReturn(Optional.of(member));
        when(lineUpRepository.findAllByMatch_Match_idAndTeam_Id(5L, 10L)).thenReturn(List.of());
        when(lineUpRepository.saveAll(anyList())).thenReturn(List.of(saved));
        when(lineUpPersistenceMapper.toModel(saved)).thenReturn(model);
        when(lineUpMapper.toDto(model)).thenReturn(dto);

        List<LineUpResponseDTO> result = service.saveLineUp(1L, 5L, request);

        assertThat(result).containsExactly(dto);
    }

    @Test
    void saveLineUp_notCaptain_throws() {
        UserEntity player = buildUserEntity(1L, Role.PLAYER);
        when(userRepository.findById(1L)).thenReturn(Optional.of(player));

        assertThatThrownBy(() -> service.saveLineUp(1L, 5L, buildRequest(10L, 2L)))
                .isInstanceOf(UserValidationException.class)
                .hasMessageContaining("captain");
    }

    @Test
    void saveLineUp_captainNotFound_throws() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.saveLineUp(99L, 5L, buildRequest(10L, 2L)))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void saveLineUp_matchNotFound_throws() {
        UserEntity captain = buildUserEntity(1L, Role.CAPTAIN);
        when(userRepository.findById(1L)).thenReturn(Optional.of(captain));
        when(matchRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.saveLineUp(1L, 99L, buildRequest(10L, 2L)))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void saveLineUp_notCaptainOfTeam_throws() {
        UserEntity captain = buildUserEntity(1L, Role.CAPTAIN);
        UserEntity otherCaptain = buildUserEntity(99L, Role.CAPTAIN);
        MatchEntity match = buildMatchEntity(5L, null, null);
        TeamEntity team = buildTeamEntity(10L, otherCaptain);

        when(userRepository.findById(1L)).thenReturn(Optional.of(captain));
        when(matchRepository.findById(5L)).thenReturn(Optional.of(match));
        when(teamRepository.findById(10L)).thenReturn(Optional.of(team));

        assertThatThrownBy(() -> service.saveLineUp(1L, 5L, buildRequest(10L, 2L)))
                .isInstanceOf(UserValidationException.class)
                .hasMessageContaining("captain of this team");
    }

    @Test
    void saveLineUp_playerNotMember_throws() {
        UserEntity captain = buildUserEntity(1L, Role.CAPTAIN);
        TeamEntity team = buildTeamEntity(10L, captain);
        MatchEntity match = buildMatchEntity(5L, team, buildTeamEntity(11L, captain));
        UserEntity player = buildUserEntity(2L, Role.PLAYER);

        when(userRepository.findById(1L)).thenReturn(Optional.of(captain));
        when(matchRepository.findById(5L)).thenReturn(Optional.of(match));
        when(teamRepository.findById(10L)).thenReturn(Optional.of(team));
        when(userRepository.findById(2L)).thenReturn(Optional.of(player));
        when(teamMemberRepository.findByTeam_IdAndUser_User_id(10L, 2L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.saveLineUp(1L, 5L, buildRequest(10L, 2L)))
                .isInstanceOf(UserValidationException.class);
    }

    @Test
    void saveLineUp_emptyPlayers_throws() {
        UserEntity captain = buildUserEntity(1L, Role.CAPTAIN);
        TeamEntity team = buildTeamEntity(10L, captain);
        MatchEntity match = buildMatchEntity(5L, team, buildTeamEntity(11L, captain));

        when(userRepository.findById(1L)).thenReturn(Optional.of(captain));
        when(matchRepository.findById(5L)).thenReturn(Optional.of(match));
        when(teamRepository.findById(10L)).thenReturn(Optional.of(team));

        LineUpRequestDTO request = LineUpRequestDTO.builder().teamId(10L).players(List.of()).build();

        assertThatThrownBy(() -> service.saveLineUp(1L, 5L, request))
                .isInstanceOf(UserValidationException.class);
    }

    // ─── getLineUp ────────────────────────────────────────────────────────────

    @Test
    void getLineUp_found_returnsList() {
        UserEntity captain = buildUserEntity(1L, Role.CAPTAIN);
        MatchEntity match = buildMatchEntity(5L, null, null);
        TeamEntity team = buildTeamEntity(10L, captain);
        UserEntity player = buildUserEntity(2L, Role.PLAYER);
        LineUpEntity entity = buildLineUpEntity(100L, match, team, player);
        LineUp model = new LineUp();
        LineUpResponseDTO dto = new LineUpResponseDTO();

        when(matchRepository.findById(5L)).thenReturn(Optional.of(match));
        when(teamRepository.findById(10L)).thenReturn(Optional.of(team));
        when(lineUpRepository.findAllByMatch_Match_idAndTeam_Id(5L, 10L)).thenReturn(List.of(entity));
        when(lineUpPersistenceMapper.toModel(entity)).thenReturn(model);
        when(lineUpMapper.toDto(model)).thenReturn(dto);

        List<LineUpResponseDTO> result = service.getLineUp(5L, 10L);

        assertThat(result).containsExactly(dto);
    }

    @Test
    void getLineUp_matchNotFound_throws() {
        when(matchRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getLineUp(99L, 10L))
                .isInstanceOf(UserNotFoundException.class);
    }

    // ─── Helpers ──────────────────────────────────────────────────────────────

    private LineUpRequestDTO buildRequest(Long teamId, Long userId) {
        LineUpEntryDTO entry = LineUpEntryDTO.builder()
                .userId(userId)
                .role(LineUpRole.TITULAR)
                .position("Portero")
                .jerseyNumber(1)
                .build();
        return LineUpRequestDTO.builder()
                .teamId(teamId)
                .players(List.of(entry))
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

    private MatchEntity buildMatchEntity(Long id, TeamEntity team1, TeamEntity team2) {
        return MatchEntity.builder()
                .match_id(id)
                .team1(team1)
                .team2(team2)
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

package edu.dosw.TECHCUP.core.validator;

import edu.dosw.TECHCUP.core.exception.UserValidationException;
import edu.dosw.TECHCUP.core.model.enums.Role;
import edu.dosw.TECHCUP.core.model.enums.TeamMemberStatus;
import edu.dosw.TECHCUP.persistence.entity.TeamMemberEntity;
import edu.dosw.TECHCUP.persistence.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.*;

class TeamValidatorTest {

    private TeamValidator validator;

    @BeforeEach
    void setUp() {
        validator = new TeamValidator();
    }

    // ─── validateTeamName ─────────────────────────────────────────────────────

    @Test
    void validateTeamName_valid_doesNotThrow() {
        assertThatCode(() -> validator.validateTeamName("Los Campeones")).doesNotThrowAnyException();
    }

    @Test
    void validateTeamName_null_throws() {
        assertThatThrownBy(() -> validator.validateTeamName(null))
                .isInstanceOf(UserValidationException.class)
                .hasMessageContaining("required");
    }

    @Test
    void validateTeamName_blank_throws() {
        assertThatThrownBy(() -> validator.validateTeamName("   "))
                .isInstanceOf(UserValidationException.class)
                .hasMessageContaining("required");
    }

    @Test
    void validateTeamName_tooLong_throws() {
        String longName = "A".repeat(51);
        assertThatThrownBy(() -> validator.validateTeamName(longName))
                .isInstanceOf(UserValidationException.class)
                .hasMessageContaining("50");
    }

    @Test
    void validateTeamName_exactly50Chars_doesNotThrow() {
        String name = "A".repeat(50);
        assertThatCode(() -> validator.validateTeamName(name)).doesNotThrowAnyException();
    }

    // ─── validateCaptainRole ──────────────────────────────────────────────────

    @Test
    void validateCaptainRole_captain_doesNotThrow() {
        UserEntity user = buildUser(Role.CAPTAIN);
        assertThatCode(() -> validator.validateCaptainRole(user)).doesNotThrowAnyException();
    }

    @Test
    void validateCaptainRole_notCaptain_throws() {
        UserEntity user = buildUser(Role.PLAYER);
        assertThatThrownBy(() -> validator.validateCaptainRole(user))
                .isInstanceOf(UserValidationException.class)
                .hasMessageContaining("Captain");
    }

    // ─── validatePlayerRole ───────────────────────────────────────────────────

    @Test
    void validatePlayerRole_player_doesNotThrow() {
        UserEntity user = buildUser(Role.PLAYER);
        assertThatCode(() -> validator.validatePlayerRole(user)).doesNotThrowAnyException();
    }

    @Test
    void validatePlayerRole_notPlayer_throws() {
        UserEntity user = buildUser(Role.CAPTAIN);
        assertThatThrownBy(() -> validator.validatePlayerRole(user))
                .isInstanceOf(UserValidationException.class)
                .hasMessageContaining("Player");
    }

    // ─── validateTeamSize ─────────────────────────────────────────────────────

    @Test
    void validateTeamSize_minimumValid_doesNotThrow() {
        List<UserEntity> members = buildUserList(7);
        assertThatCode(() -> validator.validateTeamSize(members)).doesNotThrowAnyException();
    }

    @Test
    void validateTeamSize_maximumValid_doesNotThrow() {
        List<UserEntity> members = buildUserList(12);
        assertThatCode(() -> validator.validateTeamSize(members)).doesNotThrowAnyException();
    }

    @Test
    void validateTeamSize_tooFew_throws() {
        List<UserEntity> members = buildUserList(6);
        assertThatThrownBy(() -> validator.validateTeamSize(members))
                .isInstanceOf(UserValidationException.class)
                .hasMessageContaining("7");
    }

    @Test
    void validateTeamSize_null_throws() {
        assertThatThrownBy(() -> validator.validateTeamSize(null))
                .isInstanceOf(UserValidationException.class)
                .hasMessageContaining("7");
    }

    @Test
    void validateTeamSize_tooMany_throws() {
        List<UserEntity> members = buildUserList(13);
        assertThatThrownBy(() -> validator.validateTeamSize(members))
                .isInstanceOf(UserValidationException.class)
                .hasMessageContaining("12");
    }

    // ─── validateTeamCapacity ─────────────────────────────────────────────────

    @Test
    void validateTeamCapacity_underLimit_doesNotThrow() {
        List<TeamMemberEntity> members = buildMembers(11, TeamMemberStatus.ACEPTADO);
        assertThatCode(() -> validator.validateTeamCapacity(members)).doesNotThrowAnyException();
    }

    @Test
    void validateTeamCapacity_atLimit_throws() {
        List<TeamMemberEntity> members = buildMembers(12, TeamMemberStatus.ACEPTADO);
        assertThatThrownBy(() -> validator.validateTeamCapacity(members))
                .isInstanceOf(UserValidationException.class)
                .hasMessageContaining("12");
    }

    @Test
    void validateTeamCapacity_pendingMembersIgnored_doesNotThrow() {
        List<TeamMemberEntity> members = buildMembers(12, TeamMemberStatus.PENDIENTE);
        assertThatCode(() -> validator.validateTeamCapacity(members)).doesNotThrowAnyException();
    }

    // ─── validateTeamIsActive ─────────────────────────────────────────────────

    @Test
    void validateTeamIsActive_active_doesNotThrow() {
        assertThatCode(() -> validator.validateTeamIsActive(true, "Team A")).doesNotThrowAnyException();
    }

    @Test
    void validateTeamIsActive_inactive_throws() {
        assertThatThrownBy(() -> validator.validateTeamIsActive(false, "Team A"))
                .isInstanceOf(UserValidationException.class)
                .hasMessageContaining("not active");
    }

    // ─── validateShieldUrl ────────────────────────────────────────────────────

    @Test
    void validateShieldUrl_null_doesNotThrow() {
        assertThatCode(() -> validator.validateShieldUrl(null)).doesNotThrowAnyException();
    }

    @Test
    void validateShieldUrl_blank_doesNotThrow() {
        assertThatCode(() -> validator.validateShieldUrl("")).doesNotThrowAnyException();
    }

    @Test
    void validateShieldUrl_validPng_doesNotThrow() {
        assertThatCode(() -> validator.validateShieldUrl("https://example.com/shield.png"))
                .doesNotThrowAnyException();
    }

    @Test
    void validateShieldUrl_validJpg_doesNotThrow() {
        assertThatCode(() -> validator.validateShieldUrl("https://example.com/img.jpg"))
                .doesNotThrowAnyException();
    }

    @Test
    void validateShieldUrl_invalidExtension_throws() {
        assertThatThrownBy(() -> validator.validateShieldUrl("https://example.com/file.pdf"))
                .isInstanceOf(UserValidationException.class)
                .hasMessageContaining("invalid");
    }

    @Test
    void validateShieldUrl_notUrl_throws() {
        assertThatThrownBy(() -> validator.validateShieldUrl("not-a-url"))
                .isInstanceOf(UserValidationException.class)
                .hasMessageContaining("invalid");
    }

    // ─── Helpers ──────────────────────────────────────────────────────────────

    private UserEntity buildUser(Role role) {
        return UserEntity.builder()
                .firstName("Juan")
                .lastName("Tellez")
                .userType(role)
                .build();
    }

    private List<UserEntity> buildUserList(int count) {
        return IntStream.range(0, count)
                .mapToObj(i -> UserEntity.builder().build())
                .toList();
    }

    private List<TeamMemberEntity> buildMembers(int count, TeamMemberStatus status) {
        return IntStream.range(0, count)
                .mapToObj(i -> TeamMemberEntity.builder().status(status).build())
                .toList();
    }
}

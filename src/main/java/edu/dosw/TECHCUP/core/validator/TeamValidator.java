package edu.dosw.TECHCUP.core.validator;

import edu.dosw.TECHCUP.controller.dto.request.TeamRequestDTO;
import edu.dosw.TECHCUP.core.exception.UserValidationException;
import edu.dosw.TECHCUP.core.model.TeamMember;
import edu.dosw.TECHCUP.core.model.User;
import edu.dosw.TECHCUP.core.model.enums.Role;
import edu.dosw.TECHCUP.core.model.enums.TeamMemberStatus;
import edu.dosw.TECHCUP.persistence.entity.TeamMemberEntity;
import edu.dosw.TECHCUP.persistence.entity.UserEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TeamValidator {

    private static final int MIN_PLAYERS = 7;
    private static final int MAX_PLAYERS = 12;
    private static final int MAX_NAME_LENGTH = 50;

    public void validate(TeamRequestDTO dto) {
        validateTeamName(dto.getName());
    }

    public void validateTeamName(String teamName) {
        if (teamName == null || teamName.isBlank())
            throw new UserValidationException("The team name is required.");

        if (teamName.length() > MAX_NAME_LENGTH)
            throw new UserValidationException(
                    "The team name cannot exceed " + MAX_NAME_LENGTH + " characters.");
    }

    public void validateCaptainRole(UserEntity user) {
        if (user.getUserType() != Role.CAPTAIN)
            throw new UserValidationException(
                    "The user " + user.getFirstName() + " " + user.getLastName()
                            + " He does not have the role of Captain.");
    }

    public void validateTeamSize(List<UserEntity> members) {
        if (members == null || members.size() < MIN_PLAYERS) {
            throw new UserValidationException(
                    "The team must have at least " + MIN_PLAYERS + " players.");
        }
        if (members.size() > MAX_PLAYERS) {
            throw new UserValidationException(
                    "The team cannot have more than " + MAX_PLAYERS + " players.");
        }
    }

    public void validateTeamCapacity(List<TeamMemberEntity> members) {
        long accepted = members.stream()
                .filter(m -> m.getStatus() == TeamMemberStatus.ACEPTADO)
                .count();

        if (accepted >= MAX_PLAYERS)
            throw new UserValidationException(
                    "The team already has the maximum number of " + MAX_PLAYERS + " players.");
    }

    public void validatePlayerRole(UserEntity user) {
        if (user.getUserType() != Role.PLAYER)
            throw new UserValidationException(
                    "The user " + user.getFirstName() + " " + user.getLastName()
                            + " does not have the role of Player.");
    }

    public void validateTeamIsActive(boolean active, String teamName) {
        if (!active)
            throw new UserValidationException(
                    "The team \"" + teamName + "\" is not active.");
    }

    public void validateShieldUrl(String shieldUrl) {
        if (shieldUrl != null && !shieldUrl.isBlank()
                && !shieldUrl.matches("^https?://.*\\.(png|jpg|jpeg|webp|svg)(\\?.*)?$"))
            throw new UserValidationException(
                    "The URL for the shield is invalid. It must be a URL that points to an image (png, jpg, jpeg, webp, svg).");
    }
}
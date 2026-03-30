package edu.dosw.TECHCUP.core.validator;

import edu.dosw.TECHCUP.controller.dto.request.TeamRequestDTO;
import edu.dosw.TECHCUP.core.exception.UserValidationException;
import edu.dosw.TECHCUP.core.model.TeamMember;
import edu.dosw.TECHCUP.core.model.User;
import edu.dosw.TECHCUP.core.model.enums.Role;
import edu.dosw.TECHCUP.core.model.enums.TeamMemberStatus;
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
            throw new UserValidationException("El nombre del equipo es obligatorio.");

        if (teamName.length() > MAX_NAME_LENGTH)
            throw new UserValidationException(
                    "El nombre del equipo no puede superar " + MAX_NAME_LENGTH + " caracteres.");
    }

    public void validateCaptainRole(User user) {
        if (user.getUserType() != Role.CAPTAIN)
            throw new UserValidationException(
                    "El usuario " + user.getFirstName() + " " + user.getLastName()
                            + " no tiene el rol de Capitán.");
    }

    public void validateTeamSize(List<User> activeMembers) {
        if (activeMembers == null || activeMembers.size() < MIN_PLAYERS)
            throw new UserValidationException(
                    "El equipo debe tener al menos " + MIN_PLAYERS + " jugadores activos.");

        if (activeMembers.size() > MAX_PLAYERS)
            throw new UserValidationException(
                    "El equipo no puede tener más de " + MAX_PLAYERS + " jugadores.");
    }

    public void validateTeamCapacity(List<TeamMember> members) {
        long accepted = members.stream()
                .filter(m -> m.getStatus() == TeamMemberStatus.ACEPTADO)
                .count();

        if (accepted >= MAX_PLAYERS)
            throw new UserValidationException(
                    "El equipo ya tiene el máximo de " + MAX_PLAYERS + " jugadores.");
    }

    public void validatePlayerRole(User user) {
        if (user.getUserType() != Role.PLAYER)
            throw new UserValidationException(
                    "El usuario " + user.getFirstName() + " " + user.getLastName()
                            + " no tiene el rol de Jugador.");
    }

    public void validateTeamIsActive(boolean active, String teamName) {
        if (!active)
            throw new UserValidationException(
                    "El equipo \"" + teamName + "\" no está activo.");
    }

    public void validateShieldUrl(String shieldUrl) {
        if (shieldUrl != null && !shieldUrl.isBlank()
                && !shieldUrl.matches("^https?://.*\\.(png|jpg|jpeg|webp|svg)(\\?.*)?$"))
            throw new UserValidationException(
                    "La URL del escudo no es válida. Debe ser una URL que apunte a una imagen (png, jpg, jpeg, webp, svg).");
    }
}
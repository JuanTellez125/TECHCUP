package edu.dosw.TECHCUP.core.validator;

import edu.dosw.TECHCUP.core.exception.UserValidationException;
import edu.dosw.TECHCUP.core.model.User;
import edu.dosw.TECHCUP.core.model.enums.Role;

import java.util.List;

public class TeamValidator {
    private static final int MIN_PLAYERS = 7;
    private static final int MAX_PLAYERS = 12;

    private static final List<Role> PROGRAM_ROLES = List.of(
            Role.PLAYER,
            Role.CAPTAIN
    );

    public void validateTeamSize(List<User> members) {
        if (members == null || members.size() < MIN_PLAYERS) {
            throw new UserValidationException(
                    "El equipo debe tener al menos " + MIN_PLAYERS + " jugadores.");
        }
        if (members.size() > MAX_PLAYERS) {
            throw new UserValidationException(
                    "El equipo no puede tener más de " + MAX_PLAYERS + " jugadores.");
        }
    }

    public void validateNoDuplicatePlayer(User user) {
        if (user.getTeam() != null) {
            throw new UserValidationException(
                    "El jugador " + user.getName() + " ya pertenece a otro equipo.");
        }
    }

    public void validateCaptainRole(User user) {
        if (!user.getRole().equals(Role.CAPTAIN)) {
            throw new UserValidationException(
                    "El usuario " + user.getName() + " no tiene el rol de Capitán.");
        }
    }

    public void validateTeamName(String teamName) {
        if (teamName == null || teamName.isBlank()) {
            throw new UserValidationException("El nombre del equipo es obligatorio.");
        }
        if (teamName.length() > 50) {
            throw new UserValidationException("El nombre del equipo no puede superar 50 caracteres.");
        }
    }
}

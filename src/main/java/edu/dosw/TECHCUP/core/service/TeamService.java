package edu.dosw.TECHCUP.core.service;

import edu.dosw.TECHCUP.controller.dto.request.TeamRequestDTO;
import edu.dosw.TECHCUP.controller.dto.response.TeamResponseDTO;
import edu.dosw.TECHCUP.controller.dto.response.UserResponseDTO;
import edu.dosw.TECHCUP.controller.mapper.TeamMapper;
import edu.dosw.TECHCUP.controller.mapper.UserMapper;
import edu.dosw.TECHCUP.core.exception.UserNotFoundException;
import edu.dosw.TECHCUP.core.exception.UserValidationException;
import edu.dosw.TECHCUP.core.model.Team;
import edu.dosw.TECHCUP.core.model.User;
import edu.dosw.TECHCUP.core.model.enums.PlayerAvailable;
import edu.dosw.TECHCUP.core.model.enums.Position;
import edu.dosw.TECHCUP.core.model.enums.Role;
import edu.dosw.TECHCUP.core.model.enums.TeamStatus;
import edu.dosw.TECHCUP.core.repository.TeamRepository;
import edu.dosw.TECHCUP.core.repository.UserRepository;
import edu.dosw.TECHCUP.core.util.IdGeneratorUtil;
import edu.dosw.TECHCUP.core.validator.TeamValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;
    private final TeamMapper teamMapper;
    private final UserRepository userRepository;
    private final TeamValidator teamValidator;
    private final UserMapper userMapper;

    @Transactional
    public TeamResponseDTO createTeam(String captainId, TeamRequestDTO dto) {
        User captain = userRepository.findById(captainId)
                .orElseThrow(() -> new UserNotFoundException(captainId));

        teamValidator.validateCaptainRole(captain);
        teamValidator.validateTeamName(dto.getTeamName());

        if (teamRepository.existsByTeamName(dto.getTeamName())) {
            throw new UserValidationException(
                    "Ya existe un equipo con el nombre: " + dto.getTeamName());
        }

        teamValidator.validateNoDuplicatePlayer(captain);

        Team team = Team.builder()
                .id(IdGeneratorUtil.generateId())
                .captainId(captainId)
                .teamName(dto.getTeamName())
                .mainColor(dto.getMainColor())
                .secundaryColor(dto.getSecundaryColor())
                .teamStatus(TeamStatus.ACTIVE)
                .build();

        Team saved = teamRepository.save(team);

        captain.setTeam(saved);
        userRepository.save(captain);

        log.info("Equipo creado: {} por capitán: {}", saved.getTeamName(), captainId);
        return teamMapper.toDto(saved);
    }

    public TeamResponseDTO getTeamById(String teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new UserNotFoundException(teamId));
        return teamMapper.toDto(team);
    }

    @Transactional
    public TeamResponseDTO invitePlayer(String captainId, String teamId, String playerId) {
        User captain = userRepository.findById(captainId)
                .orElseThrow(() -> new UserNotFoundException(captainId));
        teamValidator.validateCaptainRole(captain);

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new UserNotFoundException(teamId));

        User player = userRepository.findById(playerId)
                .orElseThrow(() -> new UserNotFoundException(playerId));

        teamValidator.validateNoDuplicatePlayer(player);

        int currentSize = team.getMembers() != null ? team.getMembers().size() : 0;
        if (currentSize >= 12) {
            throw new UserValidationException("El equipo ya tiene el máximo de 12 jugadores.");
        }

        player.setTeam(team);
        userRepository.save(player);

        log.info("Jugador {} agregado al equipo {}", playerId, teamId);
        return teamMapper.toDto(teamRepository.findById(teamId).orElseThrow());
    }

    public List<UserResponseDTO> searchAvailablePlayers(Position position, String name) {
        List<User> allAvailable = userRepository.findAll().stream()
                .filter(u -> PlayerAvailable.AVAILABLE.equals(u.getPlayerAvailable()))
                .filter(u -> u.getRole() == Role.PLAYER || u.getRole() == Role.CAPTAIN)
                .collect(Collectors.toList());

        if (position != null) {
            allAvailable = allAvailable.stream()
                    .filter(u -> position.equals(u.getMainPosition())
                            || position.equals(u.getSecundaryPosition()))
                    .collect(Collectors.toList());
        }

        if (name != null && !name.isBlank()) {
            String lowerName = name.toLowerCase();
            allAvailable = allAvailable.stream()
                    .filter(u -> u.getName().toLowerCase().contains(lowerName))
                    .collect(Collectors.toList());
        }

        return allAvailable.stream().map(userMapper::toDto).collect(Collectors.toList());
    }

    public List<TeamResponseDTO> getTeamsByTournament(String tournamentId) {
        return teamRepository.findAllByTournamentId(tournamentId)
                .stream()
                .map(teamMapper::toDto)
                .collect(Collectors.toList());
    }
}

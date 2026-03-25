package edu.dosw.TECHCUP.core.service;

import edu.dosw.TECHCUP.controller.dto.request.TeamRequestDTO;
import edu.dosw.TECHCUP.controller.dto.response.TeamResponseDTO;
import edu.dosw.TECHCUP.controller.mapper.TeamMapper;
import edu.dosw.TECHCUP.core.exception.UserNotFoundException;
import edu.dosw.TECHCUP.core.model.Team;
import edu.dosw.TECHCUP.core.model.User;
import edu.dosw.TECHCUP.core.model.enums.Role;
import edu.dosw.TECHCUP.core.repository.TeamRepository;
import edu.dosw.TECHCUP.core.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;
    private final TeamMapper teamMapper;
    private final UserRepository userRepository;

    public TeamResponseDTO createTeam(String captainId, TeamRequestDTO dto) {

        User user = userRepository.findById(captainId)
                .orElseThrow(() -> new UserNotFoundException("User Must Be Captain"));

        if (!user.getRole().equals(String.valueOf(Role.CAPTAIN))) {
            throw new UserNotFoundException("User does not have permission to create team");
        }

        Team team = Team.builder()
                .captainId(captainId)
                .build();

    }




}

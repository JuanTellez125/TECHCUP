package edu.dosw.TECHCUP.core.service;

import edu.dosw.TECHCUP.controller.dto.request.LineUpRequestDTO;
import edu.dosw.TECHCUP.controller.dto.response.LineUpResponseDTO;
import edu.dosw.TECHCUP.controller.mapper.LineUpMapper;
import edu.dosw.TECHCUP.core.exception.UserNotFoundException;
import edu.dosw.TECHCUP.core.exception.UserValidationException;
import edu.dosw.TECHCUP.core.model.LineUp;
import edu.dosw.TECHCUP.core.model.Match;
import edu.dosw.TECHCUP.core.model.Team;
import edu.dosw.TECHCUP.core.model.User;
import edu.dosw.TECHCUP.core.model.enums.Role;
import edu.dosw.TECHCUP.persistence.repository.LineUpRepository;
import edu.dosw.TECHCUP.persistence.repository.MatchRepository;
import edu.dosw.TECHCUP.persistence.repository.TeamRepository;
import edu.dosw.TECHCUP.persistence.repository.UserRepository;
import edu.dosw.TECHCUP.core.util.IdGeneratorUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class LineUpService {

    private final LineUpRepository lineUpRepository;
    private final LineUpMapper lineUpMapper;
    private final TeamRepository teamRepository;
    private final MatchRepository matchRepository;
    private final UserRepository userRepository;

    @Transactional
    public LineUpResponseDTO saveLineUp(String captainId, LineUpRequestDTO dto) {
        User captain = userRepository.findById(captainId)
                .orElseThrow(() -> new UserNotFoundException(captainId));

        if (!captain.getRole().equals(Role.CAPTAIN)) {
            throw new UserValidationException(
                    "Solo el capitán puede definir la alineación.");
        }

        Team team = teamRepository.findById(dto.getTeamId())
                .orElseThrow(() -> new UserNotFoundException(dto.getTeamId()));

        if (!team.getCaptainId().equals(captainId)) {
            throw new UserValidationException(
                    "Solo el capitán de este equipo puede definir su alineación.");
        }

        Match match = matchRepository.findById(dto.getMatchId())
                .orElseThrow(() -> new UserNotFoundException(dto.getMatchId()));

        if (dto.getTitulars() == null || dto.getTitulars().size() != 7) {
            throw new UserValidationException(
                    "La alineación debe tener exactamente 7 titulares.");
        }

        LineUp lineUp = lineUpRepository
                .findByTeamIdAndMatchId(dto.getTeamId(), dto.getMatchId())
                .orElse(LineUp.builder()
                        .id(IdGeneratorUtil.generateId())
                        .team(team)
                        .match(match)
                        .build());

        lineUp.setTitulars(dto.getTitulars());
        lineUp.setSubstitutes(dto.getSubstitutes());
        lineUp.setSuspended(dto.getSuspended());
        lineUp.setFormation(dto.getFormation());

        LineUp saved = lineUpRepository.save(lineUp);
        log.info("Alineación guardada para equipo {} en partido {}", dto.getTeamId(), dto.getMatchId());
        return lineUpMapper.toDto(saved);
    }

    public LineUpResponseDTO getLineUp(String teamId, String matchId) {
        LineUp lineUp = lineUpRepository.findByTeamIdAndMatchId(teamId, matchId)
                .orElseThrow(() -> new UserNotFoundException(
                        "Alineación no encontrada para equipo " + teamId + " en partido " + matchId));
        return lineUpMapper.toDto(lineUp);
    }
}

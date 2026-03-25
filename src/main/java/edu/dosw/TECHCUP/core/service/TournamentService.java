package edu.dosw.TECHCUP.core.service;

import edu.dosw.TECHCUP.controller.dto.request.TournamentRequestDTO;
import edu.dosw.TECHCUP.controller.dto.response.TournamentResponseDTO;
import edu.dosw.TECHCUP.core.exception.TournamentNotFoundException;
import edu.dosw.TECHCUP.core.exception.TournamentValidationException;
import edu.dosw.TECHCUP.core.model.enums.Role;
import edu.dosw.TECHCUP.core.model.Tournament;
import edu.dosw.TECHCUP.core.model.User;
import edu.dosw.TECHCUP.core.model.enums.TournamentStatus;
import edu.dosw.TECHCUP.core.repository.TournamentRepository;
import edu.dosw.TECHCUP.core.util.IdGeneratorUtil;
import edu.dosw.TECHCUP.core.validator.TournamentValidator;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import edu.dosw.TECHCUP.core.exception.UserNotFoundException;
import edu.dosw.TECHCUP.core.repository.UserRepository;
import edu.dosw.TECHCUP.controller.mapper.TournamentMapper;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class TournamentService {


    private final TournamentRepository tournamentRepository;
    private final TournamentMapper tournamentMapper;
    private final UserRepository userRepository;
    private final TournamentValidator tournamentValidator;

    @Transactional
    public TournamentResponseDTO createTournament(String organizerId, TournamentRequestDTO dto) {
        User organizer = userRepository.findById(organizerId)
                .orElseThrow(() -> new UserNotFoundException(organizerId));

        if (!organizer.getRole().equals(Role.ORGANIZER)) {
            throw new TournamentValidationException(
                    "El usuario no tiene permisos para crear un torneo.");
        }

        tournamentValidator.validate(dto);

        Tournament tournament = Tournament.builder()
                .id(IdGeneratorUtil.generateId())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .totalTeams(dto.getTotalTeams())
                .registrationCost(dto.getRegistrationCost())
                .status(TournamentStatus.SKETCH)
                .organizerId(organizerId)
                .build();

        Tournament saved = tournamentRepository.save(tournament);
        log.info("Torneo creado con ID: {}", saved.getId());
        return tournamentMapper.toDto(saved);
    }

    @Transactional
    public TournamentResponseDTO startTournament(String organizerId, String tournamentId) {

        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new TournamentNotFoundException(Long.parseLong(tournamentId)));

        tournamentValidator.validateStatusTransition(tournament.getStatus(), TournamentStatus.ACTIVE);

        tournament.setStatus(TournamentStatus.ACTIVE);
        return tournamentMapper.toDto(tournamentRepository.save(tournament));
    }

    @Transactional
    public TournamentResponseDTO setInProgress(String organizerId, String tournamentId) {
        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new TournamentNotFoundException(Long.parseLong(tournamentId)));

        tournamentValidator.validateStatusTransition(tournament.getStatus(), TournamentStatus.INPROGRESS);

        tournament.setStatus(TournamentStatus.INPROGRESS);
        return tournamentMapper.toDto(tournamentRepository.save(tournament));
    }

    @Transactional
    public TournamentResponseDTO finishTournament(String organizerId, String tournamentId) {
        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new TournamentNotFoundException(Long.parseLong(tournamentId)));

        tournamentValidator.validateStatusTransition(tournament.getStatus(), TournamentStatus.FINALIZED);

        tournament.setStatus(TournamentStatus.FINALIZED);
        return tournamentMapper.toDto(tournamentRepository.save(tournament));
    }

    @Transactional
    public TournamentResponseDTO configTournament(String organizerId, String tournamentId) {

        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new TournamentNotFoundException(Long.parseLong(tournamentId)));

        return tournamentMapper.toDto(tournamentRepository.save(tournament));
    }

    public TournamentResponseDTO getTournamentById(String tournamentId) {
        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new TournamentNotFoundException(Long.parseLong(tournamentId)));
        return tournamentMapper.toDto(tournament);
    }

    public List<TournamentResponseDTO> getAllTournaments() {
        return tournamentRepository.findAll()
                .stream()
                .map(tournamentMapper::toDto)
                .collect(Collectors.toList());
    }



}
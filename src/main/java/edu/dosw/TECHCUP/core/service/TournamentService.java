package edu.dosw.TECHCUP.core.service;

import edu.dosw.TECHCUP.controller.dto.request.TournamentConfigRequestDTO;
import edu.dosw.TECHCUP.controller.dto.request.TournamentRequestDTO;
import edu.dosw.TECHCUP.controller.dto.request.VenueRequestDTO;
import edu.dosw.TECHCUP.controller.dto.response.TournamentConfigResponseDTO;
import edu.dosw.TECHCUP.controller.dto.response.TournamentResponseDTO;
import edu.dosw.TECHCUP.controller.dto.response.VenueResponseDTO;
import edu.dosw.TECHCUP.controller.mapper.TournamentConfigMapper;
import edu.dosw.TECHCUP.controller.mapper.VenueMapper;
import edu.dosw.TECHCUP.core.exception.TournamentNotFoundException;
import edu.dosw.TECHCUP.core.exception.TournamentValidationException;
import edu.dosw.TECHCUP.core.model.TournamentConfig;
import edu.dosw.TECHCUP.core.model.Venue;
import edu.dosw.TECHCUP.core.model.enums.Role;
import edu.dosw.TECHCUP.core.model.Tournament;
import edu.dosw.TECHCUP.core.model.User;
import edu.dosw.TECHCUP.core.model.enums.TournamentStatus;
import edu.dosw.TECHCUP.persistence.repository.TournamentRepository;
import edu.dosw.TECHCUP.core.util.IdGeneratorUtil;
import edu.dosw.TECHCUP.core.validator.TournamentValidator;
import edu.dosw.TECHCUP.persistence.repository.TournamentConfigRepository;
import edu.dosw.TECHCUP.persistence.repository.VenueRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import edu.dosw.TECHCUP.core.exception.UserNotFoundException;
import edu.dosw.TECHCUP.persistence.repository.UserRepository;
import edu.dosw.TECHCUP.controller.mapper.TournamentMapper;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class TournamentService {


    private final TournamentRepository tournamentRepository;
    private final TournamentConfigRepository tournamentConfigRepository;
    private final VenueRepository venueRepository;
    private final TournamentMapper tournamentMapper;
    private final UserRepository userRepository;
    private final TournamentConfigMapper tournamentConfigMapper;
    private final VenueMapper venueMapper;
    private final TournamentValidator tournamentValidator;

    @Transactional
    public TournamentResponseDTO createTournament(Long organizerId, TournamentRequestDTO dto) {
        User organizer = userRepository.findById(organizerId)
                .orElseThrow(() -> new UserNotFoundException(organizerId));

        if (organizer.getUserType() != Role.ORGANIZER)
            throw new TournamentValidationException("El usuario no tiene permisos para crear un torneo.");

        tournamentValidator.validate(dto);

        Tournament tournament = Tournament.builder()
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .totalTeams(dto.getTotalTeams())
                .registrationCost(dto.getRegistrationCost())
                .status(TournamentStatus.SKETCH)
                .organizer(organizer)
                .build();

        Tournament saved = tournamentRepository.save(tournament);
        log.info("Torneo creado con ID: {}", saved.getTournament_id());
        return tournamentMapper.toDto(saved);
    }

    @Transactional
    public TournamentResponseDTO startTournament(Long organizerId, Long tournamentId) {
        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new TournamentNotFoundException(tournamentId));
        tournamentValidator.validateStatusTransition(tournament.getStatus(), TournamentStatus.ACTIVE);
        tournament.setStatus(TournamentStatus.ACTIVE);
        return tournamentMapper.toDto(tournamentRepository.save(tournament));
    }

    @Transactional
    public TournamentResponseDTO finishTournament(Long organizerId, Long tournamentId) {
        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new TournamentNotFoundException(tournamentId));
        tournamentValidator.validateStatusTransition(tournament.getStatus(), TournamentStatus.FINALIZED);
        tournament.setStatus(TournamentStatus.FINALIZED);
        return tournamentMapper.toDto(tournamentRepository.save(tournament));
    }

    @Transactional
    public TournamentConfigResponseDTO configTournament(Long organizerId, Long tournamentId,
                                                        TournamentConfigRequestDTO dto) {
        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new TournamentNotFoundException(tournamentId));

        TournamentConfig config = tournamentConfigRepository
                .findByTournament_Tournament_id(tournamentId)
                .orElse(TournamentConfig.builder().tournament(tournament).build());

        if (dto.getRulebook() != null)           config.setRulebook(dto.getRulebook());
        if (dto.getInscriptionDeadline() != null) config.setInscriptionDeadline(dto.getInscriptionDeadline());
        if (dto.getSchedules() != null)           config.setSchedules(dto.getSchedules());
        if (dto.getSanctions() != null)           config.setSanctions(dto.getSanctions());

        TournamentConfig saved = tournamentConfigRepository.save(config);
        log.info("Torneo {} configurado por organizador {}", tournamentId, organizerId);
        return tournamentConfigMapper.toDto(saved);
    }

    @Transactional
    public VenueResponseDTO addVenue(Long organizerId, Long tournamentId, VenueRequestDTO dto) {
        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new TournamentNotFoundException(tournamentId));

        Venue venue = Venue.builder()
                .tournament(tournament)
                .name(dto.getName())
                .Location(dto.getVenueLocation())
                .description(dto.getDescription())
                .build();

        return venueMapper.toDto(venueRepository.save(venue));
    }

    public TournamentResponseDTO getTournamentById(String tournamentId) {
        Tournament tournament = tournamentRepository.findById(Long.valueOf(tournamentId))
                .orElseThrow(() -> new TournamentNotFoundException(Long.parseLong(tournamentId)));
        return tournamentMapper.toDto(tournament);
    }

    public List<TournamentResponseDTO> getAllTournaments() {
        return tournamentRepository.findAll()
                .stream()
                .map(tournamentMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<TournamentResponseDTO> getFinalizedTournaments() {
        return tournamentRepository.findAllByStatus(TournamentStatus.FINALIZED)
                .stream()
                .map(tournamentMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<VenueResponseDTO> getVenuesByTournament(Long tournamentId) {
        return venueRepository.findAllByTournament_Tournament_id(tournamentId)
                .stream().map(venueMapper::toDto).collect(Collectors.toList());
    }

}
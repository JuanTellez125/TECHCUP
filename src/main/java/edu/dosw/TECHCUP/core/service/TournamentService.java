package edu.dosw.TECHCUP.core.service;

import edu.dosw.TECHCUP.controller.dto.request.TournamentConfigRequestDTO;
import edu.dosw.TECHCUP.controller.dto.request.TournamentRequestDTO;
import edu.dosw.TECHCUP.controller.dto.request.VenueRequestDTO;
import edu.dosw.TECHCUP.controller.dto.response.TournamentConfigResponseDTO;
import edu.dosw.TECHCUP.controller.dto.response.TournamentResponseDTO;
import edu.dosw.TECHCUP.controller.dto.response.VenueResponseDTO;
import edu.dosw.TECHCUP.controller.mapper.TournamentConfigMapper;
import edu.dosw.TECHCUP.controller.mapper.VenueMapper;
import edu.dosw.TECHCUP.core.exception.TournamentFinalizedException;
import edu.dosw.TECHCUP.core.exception.TournamentNotFoundException;
import edu.dosw.TECHCUP.core.exception.TournamentValidationException;
import edu.dosw.TECHCUP.core.model.enums.Role;
import edu.dosw.TECHCUP.core.model.enums.TournamentStatus;
import edu.dosw.TECHCUP.persistence.entity.TournamentConfigEntity;
import edu.dosw.TECHCUP.persistence.entity.TournamentEntity;
import edu.dosw.TECHCUP.persistence.entity.UserEntity;
import edu.dosw.TECHCUP.persistence.entity.VenueEntity;
import edu.dosw.TECHCUP.persistence.mapper.TournamentConfigPersistenceMapper;
import edu.dosw.TECHCUP.persistence.mapper.TournamentPersistenceMapper;
import edu.dosw.TECHCUP.persistence.mapper.VenuePersistenceMapper;
import edu.dosw.TECHCUP.persistence.repository.TournamentRepository;
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
    private final TournamentPersistenceMapper tournamentPersistenceMapper;
    private final TournamentConfigPersistenceMapper tournamentConfigPersistenceMapper;
    private final VenuePersistenceMapper venuePersistenceMapper;

    @Transactional
    public TournamentResponseDTO createTournament(Long organizerId, TournamentRequestDTO dto) {
        UserEntity organizer = userRepository.findById(organizerId)
                .orElseThrow(() -> new UserNotFoundException(organizerId));

        if (organizer.getUserType() != Role.ORGANIZER)
            throw new TournamentValidationException("The user does not have permission to create a tournament.");

        tournamentValidator.validate(dto);

        TournamentEntity tournament = TournamentEntity.builder()
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .totalTeams(dto.getTotalTeams())
                .registrationCost(dto.getRegistrationCost())
                .status(TournamentStatus.SKETCH)
                .organizer(organizer)
                .build();

        TournamentEntity saved = tournamentRepository.save(tournament);
        log.info("Tournament created with ID: {}", saved.getTournament_id());
        return tournamentMapper.toDto(tournamentPersistenceMapper.toModel(saved));
    }

    @Transactional
    public TournamentResponseDTO startTournament(Long organizerId, Long tournamentId) {
        TournamentEntity tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new TournamentNotFoundException(tournamentId));
        tournamentValidator.validateStatusTransition(tournament.getStatus(), TournamentStatus.ACTIVE);
        tournament.setStatus(TournamentStatus.ACTIVE);
        return tournamentMapper.toDto(tournamentPersistenceMapper.toModel(tournamentRepository.save(tournament)));
    }

    @Transactional
    public TournamentResponseDTO finishTournament(Long organizerId, Long tournamentId) {
        TournamentEntity tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new TournamentNotFoundException(tournamentId));
        tournamentValidator.validateStatusTransition(tournament.getStatus(), TournamentStatus.FINALIZED);
        tournament.setStatus(TournamentStatus.FINALIZED);
        return tournamentMapper.toDto(tournamentPersistenceMapper.toModel(tournamentRepository.save(tournament)));
    }

    @Transactional
    public TournamentConfigResponseDTO configTournament(Long organizerId, Long tournamentId,
                                                        TournamentConfigRequestDTO dto) {
        TournamentEntity tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new TournamentNotFoundException(tournamentId));

        if (tournament.getStatus() == TournamentStatus.FINALIZED){
            throw new TournamentFinalizedException("The tournament status cannot be finalized");
        }

        if (dto.getInscriptionDeadline() != null &&
                !dto.getInscriptionDeadline().isBefore(tournament.getStartDate())) {
            throw new TournamentValidationException("The inscription deadline must be before the tournament start date.");
        }


        TournamentConfigEntity config = tournamentConfigRepository
                .findByTournament_Tournament_id(tournamentId)
                .orElse(TournamentConfigEntity.builder().tournament(tournament).build());

        if (dto.getRulebook() != null)           config.setRulebook(dto.getRulebook());
        if (dto.getInscriptionDeadline() != null) config.setInscriptionDeadline(dto.getInscriptionDeadline());
        if (dto.getSchedules() != null)           config.setSchedules(dto.getSchedules());
        if (dto.getSanctions() != null)           config.setSanctions(dto.getSanctions());

        TournamentConfigEntity saved = tournamentConfigRepository.save(config);
        log.info("Tournament {} set up by organizer {}", tournamentId, organizerId);
        return tournamentConfigMapper.toDto(tournamentConfigPersistenceMapper.toModel(saved));
    }

    @Transactional
    public VenueResponseDTO addVenue(Long organizerId, Long tournamentId, VenueRequestDTO dto) {
        TournamentEntity tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new TournamentNotFoundException(tournamentId));

        VenueEntity venue = VenueEntity.builder()
                .tournament(tournament)
                .name(dto.getName())
                .location(dto.getVenueLocation())
                .description(dto.getDescription())
                .build();

        return venueMapper.toDto(venuePersistenceMapper.toModel(venueRepository.save(venue)));
    }

    public TournamentResponseDTO getTournamentById(String tournamentId) {
        TournamentEntity tournament = tournamentRepository.findById(Long.valueOf(tournamentId))
                .orElseThrow(() -> new TournamentNotFoundException(Long.parseLong(tournamentId)));
        return tournamentMapper.toDto(tournamentPersistenceMapper.toModel(tournament));
    }

    public List<TournamentResponseDTO> getAllTournaments() {
        return tournamentRepository.findAll()
                .stream()
                .map(e -> tournamentMapper.toDto(tournamentPersistenceMapper.toModel(e)))
                .collect(Collectors.toList());
    }

    public List<TournamentResponseDTO> getFinalizedTournaments() {
        return tournamentRepository.findAllByStatus(TournamentStatus.FINALIZED)
                .stream()
                .map(e -> tournamentMapper.toDto(tournamentPersistenceMapper.toModel(e)))
                .collect(Collectors.toList());
    }

    public List<VenueResponseDTO> getVenuesByTournament(Long tournamentId) {
        return venueRepository.findAllByTournament_Tournament_id(tournamentId)
                .stream()
                .map(e -> venueMapper.toDto(venuePersistenceMapper.toModel(e)))
                .collect(Collectors.toList());
    }

    public TournamentConfigResponseDTO getConfig(Long tournamentId) {
        tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new TournamentNotFoundException(tournamentId));

        TournamentConfigEntity config = tournamentConfigRepository
                .findByTournament_Tournament_id(tournamentId)
                .orElseThrow(() -> new TournamentNotFoundException(tournamentId));

        return tournamentConfigMapper.toDto(tournamentConfigPersistenceMapper.toModel(config));
    }

}
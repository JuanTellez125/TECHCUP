package edu.dosw.TECHCUP.core.service;

import edu.dosw.TECHCUP.core.exception.TournamentFinalizedException;
import edu.dosw.TECHCUP.core.exception.TournamentNotFoundException;
import edu.dosw.TECHCUP.core.exception.TournamentValidationException;
import edu.dosw.TECHCUP.core.model.*;
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
    private final UserRepository userRepository;
    private final TournamentValidator tournamentValidator;
    private final TournamentPersistenceMapper tournamentPersistenceMapper;
    private final TournamentConfigPersistenceMapper tournamentConfigPersistenceMapper;
    private final VenuePersistenceMapper venuePersistenceMapper;

    @Transactional
    public Tournament createTournament(Long organizerId, Tournament tournament) {
        UserEntity organizer = userRepository.findById(organizerId)
                .orElseThrow(() -> new UserNotFoundException(organizerId));

        if (organizer.getUserType() != Role.ORGANIZER)
            throw new TournamentValidationException("The user does not have permission to create a tournament.");

        tournamentValidator.validate(tournament);

        TournamentEntity tournamentEntity = TournamentEntity.builder()
                .startDate(tournament.getStartDate())
                .endDate(tournament.getEndDate())
                .totalTeams(tournament.getTotalTeams())
                .registrationCost(tournament.getRegistrationCost())
                .status(TournamentStatus.SKETCH)
                .organizer(organizer)
                .build();

        TournamentEntity saved = tournamentRepository.save(tournamentEntity);
        log.info("Tournament created with ID: {}", saved.getTournament_id());
        return tournamentPersistenceMapper.toModel(saved);
    }

    @Transactional
    public Tournament startTournament(Long organizerId, Long tournamentId) {
        TournamentEntity tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new TournamentNotFoundException(tournamentId));
        tournamentValidator.validateStatusTransition(tournament.getStatus(), TournamentStatus.ACTIVE);
        tournament.setStatus(TournamentStatus.ACTIVE);
        return tournamentPersistenceMapper.toModel(tournamentRepository.save(tournament));
    }

    @Transactional
    public Tournament finishTournament(Long organizerId, Long tournamentId) {
        TournamentEntity tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new TournamentNotFoundException(tournamentId));
        tournamentValidator.validateStatusTransition(tournament.getStatus(), TournamentStatus.FINALIZED);
        tournament.setStatus(TournamentStatus.FINALIZED);
        return tournamentPersistenceMapper.toModel(tournamentRepository.save(tournament));
    }

    @Transactional
    public TournamentConfig configTournament(Long organizerId, Long tournamentId, TournamentConfig configData) {
        TournamentEntity tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new TournamentNotFoundException(tournamentId));

        if (tournament.getStatus() == TournamentStatus.FINALIZED) {
            throw new TournamentFinalizedException("The tournament status cannot be finalized");
        }

        if (configData.getInscriptionDeadline() != null &&
                !configData.getInscriptionDeadline().isBefore(tournament.getStartDate())) {
            throw new TournamentValidationException("The inscription deadline must be before the tournament start date.");
        }

        TournamentConfigEntity config = tournamentConfigRepository
                .findByTournament_Tournament_id(tournamentId)
                .orElse(TournamentConfigEntity.builder().tournament(tournament).build());

        if (configData.getRulebook() != null)           config.setRulebook(configData.getRulebook());
        if (configData.getInscriptionDeadline() != null) config.setInscriptionDeadline(configData.getInscriptionDeadline());
        if (configData.getSchedules() != null)           config.setSchedules(configData.getSchedules());
        if (configData.getSanctions() != null)           config.setSanctions(configData.getSanctions());

        TournamentConfigEntity saved = tournamentConfigRepository.save(config);
        log.info("Tournament {} set up by organizer {}", tournamentId, organizerId);
        return tournamentConfigPersistenceMapper.toModel(saved);
    }

    @Transactional
    public Venue addVenue(Long organizerId, Long tournamentId, Venue venueData) {
        TournamentEntity tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new TournamentNotFoundException(tournamentId));

        VenueEntity venueEntity = VenueEntity.builder()
                .tournament(tournament)
                .name(venueData.getName())
                .location(venueData.getLocation())
                .description(venueData.getDescription())
                .build();

        return venuePersistenceMapper.toModel(venueRepository.save(venueEntity));
    }

    public Tournament getTournamentById(String tournamentId) {
        TournamentEntity tournament = tournamentRepository.findById(Long.valueOf(tournamentId))
                .orElseThrow(() -> new TournamentNotFoundException(Long.parseLong(tournamentId)));
        return tournamentPersistenceMapper.toModel(tournament);
    }

    public List<Tournament> getAllTournaments() {
        return tournamentRepository.findAll()
                .stream()
                .map(tournamentPersistenceMapper::toModel)
                .collect(Collectors.toList());
    }

    public List<Tournament> getFinalizedTournaments() {
        return tournamentRepository.findAllByStatus(TournamentStatus.FINALIZED)
                .stream()
                .map(tournamentPersistenceMapper::toModel)
                .collect(Collectors.toList());
    }

    public List<Venue> getVenuesByTournament(Long tournamentId) {
        return venueRepository.findAllByTournament_Tournament_id(tournamentId)
                .stream()
                .map(venuePersistenceMapper::toModel)
                .collect(Collectors.toList());
    }

    public TournamentConfig getConfig(Long tournamentId) {
        tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new TournamentNotFoundException(tournamentId));

        TournamentConfigEntity config = tournamentConfigRepository
                .findByTournament_Tournament_id(tournamentId)
                .orElseThrow(() -> new TournamentNotFoundException(tournamentId));

        return tournamentConfigPersistenceMapper.toModel(config);
    }
}

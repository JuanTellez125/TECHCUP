package edu.dosw.TECHCUP.core.service;

import edu.dosw.TECHCUP.core.exception.TournamentNotFoundException;
import edu.dosw.TECHCUP.core.exception.TournamentValidationException;
import edu.dosw.TECHCUP.core.exception.UserNotFoundException;
import edu.dosw.TECHCUP.core.model.Tournament;
import edu.dosw.TECHCUP.core.model.TournamentConfig;
import edu.dosw.TECHCUP.core.model.Venue;
import edu.dosw.TECHCUP.core.model.enums.Role;
import edu.dosw.TECHCUP.core.model.enums.TournamentStatus;
import edu.dosw.TECHCUP.core.validator.TournamentValidator;
import edu.dosw.TECHCUP.persistence.entity.TournamentConfigEntity;
import edu.dosw.TECHCUP.persistence.entity.TournamentEntity;
import edu.dosw.TECHCUP.persistence.entity.UserEntity;
import edu.dosw.TECHCUP.persistence.entity.VenueEntity;
import edu.dosw.TECHCUP.persistence.mapper.TournamentConfigPersistenceMapper;
import edu.dosw.TECHCUP.persistence.mapper.TournamentPersistenceMapper;
import edu.dosw.TECHCUP.persistence.mapper.VenuePersistenceMapper;
import edu.dosw.TECHCUP.persistence.repository.TournamentConfigRepository;
import edu.dosw.TECHCUP.persistence.repository.TournamentRepository;
import edu.dosw.TECHCUP.persistence.repository.UserRepository;
import edu.dosw.TECHCUP.persistence.repository.VenueRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TournamentServiceTest {

    @Mock TournamentRepository tournamentRepository;
    @Mock TournamentConfigRepository tournamentConfigRepository;
    @Mock VenueRepository venueRepository;
    @Mock UserRepository userRepository;
    @Mock TournamentValidator tournamentValidator;
    @Mock TournamentPersistenceMapper tournamentPersistenceMapper;
    @Mock TournamentConfigPersistenceMapper tournamentConfigPersistenceMapper;
    @Mock VenuePersistenceMapper venuePersistenceMapper;

    @InjectMocks TournamentService service;

    // ─── createTournament ─────────────────────────────────────────────────────

    @Test
    void createTournament_success() {
        UserEntity organizer = buildOrganizerEntity(1L);
        TournamentEntity saved = buildTournamentEntity(10L, TournamentStatus.SKETCH);
        Tournament model = new Tournament();
        Tournament tournamentInput = buildTournamentModel();

        when(userRepository.findById(1L)).thenReturn(Optional.of(organizer));
        when(tournamentRepository.save(any())).thenReturn(saved);
        when(tournamentPersistenceMapper.toModel(saved)).thenReturn(model);

        Tournament result = service.createTournament(1L, tournamentInput);

        assertThat(result).isEqualTo(model);
        verify(tournamentValidator).validate(tournamentInput);
    }

    @Test
    void createTournament_notOrganizer_throws() {
        UserEntity player = buildUserEntity(1L, Role.PLAYER);
        when(userRepository.findById(1L)).thenReturn(Optional.of(player));

        assertThatThrownBy(() -> service.createTournament(1L, buildTournamentModel()))
                .isInstanceOf(TournamentValidationException.class)
                .hasMessageContaining("permission");
    }

    @Test
    void createTournament_organizerNotFound_throws() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.createTournament(99L, buildTournamentModel()))
                .isInstanceOf(UserNotFoundException.class);
    }

    // ─── startTournament ──────────────────────────────────────────────────────

    @Test
    void startTournament_success() {
        TournamentEntity tournament = buildTournamentEntity(10L, TournamentStatus.SKETCH);
        Tournament model = new Tournament();

        when(tournamentRepository.findById(10L)).thenReturn(Optional.of(tournament));
        when(tournamentRepository.save(tournament)).thenReturn(tournament);
        when(tournamentPersistenceMapper.toModel(tournament)).thenReturn(model);

        Tournament result = service.startTournament(1L, 10L);

        assertThat(result).isEqualTo(model);
        assertThat(tournament.getStatus()).isEqualTo(TournamentStatus.ACTIVE);
        verify(tournamentValidator).validateStatusTransition(TournamentStatus.SKETCH, TournamentStatus.ACTIVE);
    }

    @Test
    void startTournament_notFound_throws() {
        when(tournamentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.startTournament(1L, 99L))
                .isInstanceOf(TournamentNotFoundException.class);
    }

    // ─── finishTournament ─────────────────────────────────────────────────────

    @Test
    void finishTournament_success() {
        TournamentEntity tournament = buildTournamentEntity(10L, TournamentStatus.INPROGRESS);
        Tournament model = new Tournament();

        when(tournamentRepository.findById(10L)).thenReturn(Optional.of(tournament));
        when(tournamentRepository.save(tournament)).thenReturn(tournament);
        when(tournamentPersistenceMapper.toModel(tournament)).thenReturn(model);

        Tournament result = service.finishTournament(1L, 10L);

        assertThat(result).isEqualTo(model);
        assertThat(tournament.getStatus()).isEqualTo(TournamentStatus.FINALIZED);
        verify(tournamentValidator).validateStatusTransition(TournamentStatus.INPROGRESS, TournamentStatus.FINALIZED);
    }

    @Test
    void finishTournament_notFound_throws() {
        when(tournamentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.finishTournament(1L, 99L))
                .isInstanceOf(TournamentNotFoundException.class);
    }

    // ─── configTournament ─────────────────────────────────────────────────────

    @Test
    void configTournament_createsNewConfig() {
        TournamentEntity tournament = buildTournamentEntity(10L, TournamentStatus.ACTIVE);
        TournamentConfig configInput = TournamentConfig.builder().rulebook("Rules v1").build();
        TournamentConfigEntity saved = TournamentConfigEntity.builder().tournament(tournament).build();
        TournamentConfig model = new TournamentConfig();

        when(tournamentRepository.findById(10L)).thenReturn(Optional.of(tournament));
        when(tournamentConfigRepository.findByTournament_Tournament_id(10L)).thenReturn(Optional.empty());
        when(tournamentConfigRepository.save(any())).thenReturn(saved);
        when(tournamentConfigPersistenceMapper.toModel(saved)).thenReturn(model);

        TournamentConfig result = service.configTournament(1L, 10L, configInput);

        assertThat(result).isEqualTo(model);
    }

    @Test
    void configTournament_tournamentNotFound_throws() {
        when(tournamentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.configTournament(1L, 99L, new TournamentConfig()))
                .isInstanceOf(TournamentNotFoundException.class);
    }

    // ─── addVenue ─────────────────────────────────────────────────────────────

    @Test
    void addVenue_success() {
        TournamentEntity tournament = buildTournamentEntity(10L, TournamentStatus.ACTIVE);
        VenueEntity saved = VenueEntity.builder().tournament(tournament).name("Stadium A").build();
        Venue model = new Venue();
        Venue venueInput = Venue.builder()
                .name("Stadium A")
                .location("Bogota")
                .description("Main stadium")
                .build();

        when(tournamentRepository.findById(10L)).thenReturn(Optional.of(tournament));
        when(venueRepository.save(any())).thenReturn(saved);
        when(venuePersistenceMapper.toModel(saved)).thenReturn(model);

        Venue result = service.addVenue(1L, 10L, venueInput);

        assertThat(result).isEqualTo(model);
    }

    @Test
    void addVenue_tournamentNotFound_throws() {
        when(tournamentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.addVenue(1L, 99L, new Venue()))
                .isInstanceOf(TournamentNotFoundException.class);
    }

    // ─── getTournamentById / getAllTournaments / getFinalizedTournaments ───────

    @Test
    void getTournamentById_found_returnsModel() {
        TournamentEntity tournament = buildTournamentEntity(10L, TournamentStatus.ACTIVE);
        Tournament model = new Tournament();

        when(tournamentRepository.findById(10L)).thenReturn(Optional.of(tournament));
        when(tournamentPersistenceMapper.toModel(tournament)).thenReturn(model);

        Tournament result = service.getTournamentById("10");

        assertThat(result).isEqualTo(model);
    }

    @Test
    void getTournamentById_notFound_throws() {
        when(tournamentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getTournamentById("99"))
                .isInstanceOf(TournamentNotFoundException.class);
    }

    @Test
    void getAllTournaments_returnsMappedList() {
        TournamentEntity e1 = buildTournamentEntity(1L, TournamentStatus.ACTIVE);
        TournamentEntity e2 = buildTournamentEntity(2L, TournamentStatus.SKETCH);
        Tournament m1 = new Tournament();
        Tournament m2 = new Tournament();

        when(tournamentRepository.findAll()).thenReturn(List.of(e1, e2));
        when(tournamentPersistenceMapper.toModel(e1)).thenReturn(m1);
        when(tournamentPersistenceMapper.toModel(e2)).thenReturn(m2);

        List<Tournament> result = service.getAllTournaments();

        assertThat(result).hasSize(2).contains(m1, m2);
    }

    @Test
    void getFinalizedTournaments_returnsOnlyFinalized() {
        TournamentEntity e1 = buildTournamentEntity(1L, TournamentStatus.FINALIZED);
        Tournament m1 = new Tournament();

        when(tournamentRepository.findAllByStatus(TournamentStatus.FINALIZED)).thenReturn(List.of(e1));
        when(tournamentPersistenceMapper.toModel(e1)).thenReturn(m1);

        List<Tournament> result = service.getFinalizedTournaments();

        assertThat(result).hasSize(1).contains(m1);
    }

    @Test
    void getVenuesByTournament_returnsMappedList() {
        VenueEntity v1 = VenueEntity.builder().name("Field A").build();
        Venue model = new Venue();

        when(venueRepository.findAllByTournament_Tournament_id(10L)).thenReturn(List.of(v1));
        when(venuePersistenceMapper.toModel(v1)).thenReturn(model);

        List<Venue> result = service.getVenuesByTournament(10L);

        assertThat(result).hasSize(1).contains(model);
    }

    // ─── Helpers ──────────────────────────────────────────────────────────────

    private Tournament buildTournamentModel() {
        return Tournament.builder()
                .startDate(LocalDate.now().plusDays(1))
                .endDate(LocalDate.now().plusDays(30))
                .totalTeams(8)
                .registrationCost(50.0)
                .build();
    }

    private TournamentEntity buildTournamentEntity(Long id, TournamentStatus status) {
        return TournamentEntity.builder()
                .tournament_id(id)
                .startDate(LocalDate.now().plusDays(1))
                .endDate(LocalDate.now().plusDays(30))
                .totalTeams(8)
                .registrationCost(50.0)
                .status(status)
                .build();
    }

    private UserEntity buildOrganizerEntity(Long id) {
        return buildUserEntity(id, Role.ORGANIZER);
    }

    private UserEntity buildUserEntity(Long id, Role role) {
        return UserEntity.builder()
                .user_id(id)
                .firstName("Ana")
                .lastName("Lopez")
                .email("ana@gmail.com")
                .userType(role)
                .active(true)
                .build();
    }
}

package edu.dosw.TECHCUP.core.service;

import edu.dosw.TECHCUP.core.exception.TournamentNotFoundException;
import edu.dosw.TECHCUP.core.exception.TournamentValidationException;
import edu.dosw.TECHCUP.core.exception.UserNotFoundException;
import edu.dosw.TECHCUP.core.exception.UserValidationException;
import edu.dosw.TECHCUP.core.model.Payment;
import edu.dosw.TECHCUP.core.model.Team;
import edu.dosw.TECHCUP.core.model.Tournament;
import edu.dosw.TECHCUP.core.model.TournamentRegistration;
import edu.dosw.TECHCUP.core.model.enums.PaymentStatus;
import edu.dosw.TECHCUP.core.model.enums.RegisterTournamentStatus;
import edu.dosw.TECHCUP.core.model.enums.Role;
import edu.dosw.TECHCUP.persistence.entity.*;
import edu.dosw.TECHCUP.persistence.mapper.PaymentPersistenceMapper;
import edu.dosw.TECHCUP.persistence.mapper.TournamentRegistrationPersistenceMapper;
import edu.dosw.TECHCUP.persistence.repository.*;
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
class PaymentServiceTest {

    @Mock PaymentRepository paymentRepository;
    @Mock TournamentRegistrationRepository registrationRepository;
    @Mock TeamRepository teamRepository;
    @Mock TournamentRepository tournamentRepository;
    @Mock UserRepository userRepository;
    @Mock PaymentPersistenceMapper paymentPersistenceMapper;
    @Mock TournamentRegistrationPersistenceMapper registrationPersistenceMapper;

    @InjectMocks PaymentService service;

    // ─── registerTeam ─────────────────────────────────────────────────────────

    @Test
    void registerTeam_success() {
        UserEntity captain = buildUserEntity(1L, Role.CAPTAIN);
        TeamEntity team = buildTeamEntity(10L, captain);
        TournamentEntity tournament = buildTournamentEntity(20L);
        TournamentRegistrationEntity saved = buildRegistrationEntity(100L, tournament, team);
        TournamentRegistration model = new TournamentRegistration();
        TournamentRegistration registrationInput = buildRegistrationModel(10L, 20L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(captain));
        when(teamRepository.findById(10L)).thenReturn(Optional.of(team));
        when(tournamentRepository.findById(20L)).thenReturn(Optional.of(tournament));
        when(registrationRepository.findByTeam_IdAndTournament_Tournament_id(10L, 20L)).thenReturn(Optional.empty());
        when(registrationRepository.save(any())).thenReturn(saved);
        when(registrationPersistenceMapper.toModel(saved)).thenReturn(model);

        TournamentRegistration result = service.registerTeam(1L, registrationInput);

        assertThat(result).isEqualTo(model);
    }

    @Test
    void registerTeam_notCaptain_throws() {
        UserEntity player = buildUserEntity(1L, Role.PLAYER);
        when(userRepository.findById(1L)).thenReturn(Optional.of(player));

        assertThatThrownBy(() -> service.registerTeam(1L, buildRegistrationModel(10L, 20L)))
                .isInstanceOf(UserValidationException.class)
                .hasMessageContaining("captain");
    }

    @Test
    void registerTeam_captainNotFound_throws() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.registerTeam(99L, buildRegistrationModel(10L, 20L)))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void registerTeam_notCaptainOfTeam_throws() {
        UserEntity captain = buildUserEntity(1L, Role.CAPTAIN);
        UserEntity otherCaptain = buildUserEntity(99L, Role.CAPTAIN);
        TeamEntity team = buildTeamEntity(10L, otherCaptain);

        when(userRepository.findById(1L)).thenReturn(Optional.of(captain));
        when(teamRepository.findById(10L)).thenReturn(Optional.of(team));

        assertThatThrownBy(() -> service.registerTeam(1L, buildRegistrationModel(10L, 20L)))
                .isInstanceOf(UserValidationException.class)
                .hasMessageContaining("captain");
    }

    @Test
    void registerTeam_alreadyRegistered_throws() {
        UserEntity captain = buildUserEntity(1L, Role.CAPTAIN);
        TeamEntity team = buildTeamEntity(10L, captain);
        TournamentEntity tournament = buildTournamentEntity(20L);
        TournamentRegistrationEntity existing = buildRegistrationEntity(100L, tournament, team);

        when(userRepository.findById(1L)).thenReturn(Optional.of(captain));
        when(teamRepository.findById(10L)).thenReturn(Optional.of(team));
        when(tournamentRepository.findById(20L)).thenReturn(Optional.of(tournament));
        when(registrationRepository.findByTeam_IdAndTournament_Tournament_id(10L, 20L))
                .thenReturn(Optional.of(existing));

        assertThatThrownBy(() -> service.registerTeam(1L, buildRegistrationModel(10L, 20L)))
                .isInstanceOf(UserValidationException.class)
                .hasMessageContaining("already registered");
    }

    // ─── submitPayment ────────────────────────────────────────────────────────

    @Test
    void submitPayment_success() {
        UserEntity captain = buildUserEntity(1L, Role.CAPTAIN);
        TeamEntity team = buildTeamEntity(10L, captain);
        TournamentEntity tournament = buildTournamentEntity(20L);
        TournamentRegistrationEntity registration = buildRegistrationEntity(100L, tournament, team);
        PaymentEntity saved = buildPaymentEntity(200L, registration, captain);
        Payment model = new Payment();
        Payment paymentInput = buildPaymentModel(100L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(captain));
        when(registrationRepository.findById(100L)).thenReturn(Optional.of(registration));
        when(paymentRepository.findByRegistration_TournamentRegistration_id(100L)).thenReturn(Optional.empty());
        when(paymentRepository.save(any())).thenReturn(saved);
        when(paymentPersistenceMapper.toModel(saved)).thenReturn(model);

        Payment result = service.submitPayment(1L, paymentInput);

        assertThat(result).isEqualTo(model);
        assertThat(registration.getStatus()).isEqualTo(RegisterTournamentStatus.EN_REVISION);
    }

    @Test
    void submitPayment_notCaptain_throws() {
        UserEntity player = buildUserEntity(1L, Role.PLAYER);
        when(userRepository.findById(1L)).thenReturn(Optional.of(player));

        assertThatThrownBy(() -> service.submitPayment(1L, buildPaymentModel(100L)))
                .isInstanceOf(UserValidationException.class)
                .hasMessageContaining("captain");
    }

    @Test
    void submitPayment_paymentAlreadyExists_throws() {
        UserEntity captain = buildUserEntity(1L, Role.CAPTAIN);
        TeamEntity team = buildTeamEntity(10L, captain);
        TournamentEntity tournament = buildTournamentEntity(20L);
        TournamentRegistrationEntity registration = buildRegistrationEntity(100L, tournament, team);
        PaymentEntity existing = buildPaymentEntity(200L, registration, captain);

        when(userRepository.findById(1L)).thenReturn(Optional.of(captain));
        when(registrationRepository.findById(100L)).thenReturn(Optional.of(registration));
        when(paymentRepository.findByRegistration_TournamentRegistration_id(100L))
                .thenReturn(Optional.of(existing));

        assertThatThrownBy(() -> service.submitPayment(1L, buildPaymentModel(100L)))
                .isInstanceOf(UserValidationException.class)
                .hasMessageContaining("proof");
    }

    // ─── approvePayment ───────────────────────────────────────────────────────

    @Test
    void approvePayment_success() {
        UserEntity organizer = buildUserEntity(1L, Role.ORGANIZER);
        UserEntity captain = buildUserEntity(2L, Role.CAPTAIN);
        TeamEntity team = buildTeamEntity(10L, captain);
        TournamentEntity tournament = buildTournamentEntity(20L);
        TournamentRegistrationEntity registration = buildRegistrationEntity(100L, tournament, team);
        PaymentEntity payment = buildPaymentEntity(200L, registration, captain);
        payment.setStatus(PaymentStatus.IN_REVIEW);
        Payment model = new Payment();

        when(userRepository.findById(1L)).thenReturn(Optional.of(organizer));
        when(paymentRepository.findById(200L)).thenReturn(Optional.of(payment));
        when(paymentRepository.save(payment)).thenReturn(payment);
        when(paymentPersistenceMapper.toModel(payment)).thenReturn(model);

        Payment result = service.approvePayment(1L, 200L);

        assertThat(result).isEqualTo(model);
        assertThat(payment.getStatus()).isEqualTo(PaymentStatus.APPROVED);
        assertThat(registration.getStatus()).isEqualTo(RegisterTournamentStatus.APROBADO);
    }

    @Test
    void approvePayment_notOrganizer_throws() {
        UserEntity player = buildUserEntity(1L, Role.PLAYER);
        when(userRepository.findById(1L)).thenReturn(Optional.of(player));

        assertThatThrownBy(() -> service.approvePayment(1L, 200L))
                .isInstanceOf(TournamentValidationException.class)
                .hasMessageContaining("organizer");
    }

    // ─── rejectPayment ────────────────────────────────────────────────────────

    @Test
    void rejectPayment_success() {
        UserEntity organizer = buildUserEntity(1L, Role.ORGANIZER);
        UserEntity captain = buildUserEntity(2L, Role.CAPTAIN);
        TeamEntity team = buildTeamEntity(10L, captain);
        TournamentEntity tournament = buildTournamentEntity(20L);
        TournamentRegistrationEntity registration = buildRegistrationEntity(100L, tournament, team);
        PaymentEntity payment = buildPaymentEntity(200L, registration, captain);
        Payment model = new Payment();

        when(userRepository.findById(1L)).thenReturn(Optional.of(organizer));
        when(paymentRepository.findById(200L)).thenReturn(Optional.of(payment));
        when(paymentRepository.save(payment)).thenReturn(payment);
        when(paymentPersistenceMapper.toModel(payment)).thenReturn(model);

        Payment result = service.rejectPayment(1L, 200L, "Invalid voucher");

        assertThat(result).isEqualTo(model);
        assertThat(payment.getStatus()).isEqualTo(PaymentStatus.REJECTED);
        assertThat(payment.getRejectionReason()).isEqualTo("Invalid voucher");
        assertThat(registration.getStatus()).isEqualTo(RegisterTournamentStatus.RECHAZADO);
    }

    // ─── getPaymentsByTournament / getRegistrationsByTournament ───────────────

    @Test
    void getPaymentsByTournament_returnsMappedList() {
        UserEntity organizer = buildUserEntity(1L, Role.ORGANIZER);
        UserEntity captain = buildUserEntity(2L, Role.CAPTAIN);
        TeamEntity team = buildTeamEntity(10L, captain);
        TournamentEntity tournament = buildTournamentEntity(20L);
        TournamentRegistrationEntity registration = buildRegistrationEntity(100L, tournament, team);
        PaymentEntity payment = buildPaymentEntity(200L, registration, captain);
        Payment model = new Payment();

        when(userRepository.findById(1L)).thenReturn(Optional.of(organizer));
        when(paymentRepository.findAllByRegistration_Tournament_Tournament_id(20L))
                .thenReturn(List.of(payment));
        when(paymentPersistenceMapper.toModel(payment)).thenReturn(model);

        List<Payment> result = service.getPaymentsByTournament(1L, 20L);

        assertThat(result).hasSize(1).contains(model);
    }

    @Test
    void getRegistrationsByTournament_returnsMappedList() {
        UserEntity captain = buildUserEntity(2L, Role.CAPTAIN);
        TeamEntity team = buildTeamEntity(10L, captain);
        TournamentEntity tournament = buildTournamentEntity(20L);
        TournamentRegistrationEntity reg = buildRegistrationEntity(100L, tournament, team);
        TournamentRegistration model = new TournamentRegistration();

        when(registrationRepository.findAllByTournament_Tournament_id(20L)).thenReturn(List.of(reg));
        when(registrationPersistenceMapper.toModel(reg)).thenReturn(model);

        List<TournamentRegistration> result = service.getRegistrationsByTournament(20L);

        assertThat(result).hasSize(1).contains(model);
    }

    // ─── Helpers ──────────────────────────────────────────────────────────────

    private TournamentRegistration buildRegistrationModel(Long teamId, Long tournamentId) {
        return TournamentRegistration.builder()
                .team(Team.builder().id(teamId).build())
                .tournament(Tournament.builder().tournamentId(tournamentId).build())
                .build();
    }

    private Payment buildPaymentModel(Long registrationId) {
        return Payment.builder()
                .registration(TournamentRegistration.builder()
                        .tournamentRegistrationId(registrationId)
                        .build())
                .fileUrl("http://proof.jpg")
                .paymentMethod("TRANSFER")
                .build();
    }

    private UserEntity buildUserEntity(Long id, Role role) {
        return UserEntity.builder()
                .user_id(id)
                .firstName("Test")
                .lastName("User")
                .email("test" + id + "@gmail.com")
                .userType(role)
                .active(true)
                .build();
    }

    private TeamEntity buildTeamEntity(Long id, UserEntity captain) {
        return TeamEntity.builder()
                .id(id)
                .name("Team " + id)
                .captain(captain)
                .active(true)
                .build();
    }

    private TournamentEntity buildTournamentEntity(Long id) {
        return TournamentEntity.builder()
                .tournament_id(id)
                .startDate(LocalDate.now().plusDays(1))
                .endDate(LocalDate.now().plusDays(30))
                .totalTeams(8)
                .registrationCost(50.0)
                .build();
    }

    private TournamentRegistrationEntity buildRegistrationEntity(Long id,
                                                                   TournamentEntity tournament,
                                                                   TeamEntity team) {
        return TournamentRegistrationEntity.builder()
                .tournamentRegistration_id(id)
                .tournament(tournament)
                .team(team)
                .status(RegisterTournamentStatus.PENDIENTE)
                .build();
    }

    private PaymentEntity buildPaymentEntity(Long id, TournamentRegistrationEntity registration,
                                              UserEntity uploadedBy) {
        return PaymentEntity.builder()
                .payment_id(id)
                .registration(registration)
                .uploadedBy(uploadedBy)
                .fileUrl("http://proof.jpg")
                .paymentMethod("TRANSFER")
                .status(PaymentStatus.IN_REVIEW)
                .build();
    }
}

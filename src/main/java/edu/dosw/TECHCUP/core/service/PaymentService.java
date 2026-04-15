package edu.dosw.TECHCUP.core.service;

import edu.dosw.TECHCUP.core.exception.TournamentNotFoundException;
import edu.dosw.TECHCUP.core.exception.TournamentValidationException;
import edu.dosw.TECHCUP.core.exception.UserNotFoundException;
import edu.dosw.TECHCUP.core.exception.UserValidationException;
import edu.dosw.TECHCUP.core.model.*;
import edu.dosw.TECHCUP.core.model.enums.PaymentStatus;
import edu.dosw.TECHCUP.core.model.enums.RegisterTournamentStatus;
import edu.dosw.TECHCUP.core.model.enums.Role;
import edu.dosw.TECHCUP.persistence.entity.*;
import edu.dosw.TECHCUP.persistence.mapper.PaymentPersistenceMapper;
import edu.dosw.TECHCUP.persistence.mapper.TournamentRegistrationPersistenceMapper;
import edu.dosw.TECHCUP.persistence.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final TournamentRegistrationRepository registrationRepository;
    private final TeamRepository teamRepository;
    private final TournamentRepository tournamentRepository;
    private final UserRepository userRepository;
    private final PaymentPersistenceMapper paymentPersistenceMapper;
    private final TournamentRegistrationPersistenceMapper registrationPersistenceMapper;

    @Transactional
    public TournamentRegistration registerTeam(Long captainId, TournamentRegistration registration) {
        UserEntity captain = userRepository.findById(captainId)
                .orElseThrow(() -> new UserNotFoundException(captainId));
        if (captain.getUserType() != Role.CAPTAIN)
            throw new UserValidationException("Only the captain can register the team");

        TeamEntity team = teamRepository.findById(registration.getTeam().getId())
                .orElseThrow(() -> new UserNotFoundException(registration.getTeam().getId()));

        if (!team.getCaptain().getUser_id().equals(captainId))
            throw new UserValidationException("You are not the captain of this team.");

        TournamentEntity tournament = tournamentRepository.findById(registration.getTournament().getTournamentId())
                .orElseThrow(() -> new TournamentNotFoundException(registration.getTournament().getTournamentId()));

        if (registrationRepository.findByTeam_IdAndTournament_Tournament_id(
                registration.getTeam().getId(), registration.getTournament().getTournamentId()).isPresent())
            throw new UserValidationException("The team is already registered for this tournament.");

        TournamentRegistrationEntity registrationEntity = TournamentRegistrationEntity.builder()
                .tournament(tournament)
                .team(team)
                .status(RegisterTournamentStatus.PENDIENTE)
                .registeredAt(LocalDateTime.now())
                .build();

        TournamentRegistrationEntity saved = registrationRepository.save(registrationEntity);
        log.info("Team {} registered in tournament {}", registration.getTeam().getId(), registration.getTournament().getTournamentId());
        return registrationPersistenceMapper.toModel(saved);
    }

    @Transactional
    public Payment submitPayment(Long captainId, Payment payment) {
        UserEntity captain = userRepository.findById(captainId)
                .orElseThrow(() -> new UserNotFoundException(captainId));
        if (captain.getUserType() != Role.CAPTAIN)
            throw new UserValidationException("Only the captain can upload the receipt.");

        TournamentRegistrationEntity registration = registrationRepository
                .findById(payment.getRegistration().getTournamentRegistrationId())
                .orElseThrow(() -> new UserNotFoundException(payment.getRegistration().getTournamentRegistrationId()));

        if (!registration.getTeam().getCaptain().getUser_id().equals(captainId))
            throw new UserValidationException("You are not the captain of this team..");

        if (paymentRepository.findByRegistration_TournamentRegistration_id(
                payment.getRegistration().getTournamentRegistrationId()).isPresent())
            throw new UserValidationException("There is already proof of this registration.");

        registration.setStatus(RegisterTournamentStatus.EN_REVISION);
        registrationRepository.save(registration);

        PaymentEntity paymentEntity = PaymentEntity.builder()
                .registration(registration)
                .uploadedBy(captain)
                .fileUrl(payment.getFileUrl())
                .paymentMethod(payment.getPaymentMethod())
                .status(PaymentStatus.IN_REVIEW)
                .build();

        PaymentEntity saved = paymentRepository.save(paymentEntity);
        log.info("Proof uploaded by captain {} for registration {}", captainId, payment.getRegistration().getTournamentRegistrationId());
        return paymentPersistenceMapper.toModel(saved);
    }

    @Transactional
    public Payment approvePayment(Long organizerId, Long paymentId) {
        validateOrganizer(organizerId);

        PaymentEntity payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new UserNotFoundException(paymentId));

        payment.setStatus(PaymentStatus.APPROVED);
        payment.setReviewedBy(userRepository.findById(organizerId).orElse(null));
        payment.getRegistration().setStatus(RegisterTournamentStatus.APROBADO);
        registrationRepository.save(payment.getRegistration());

        log.info("Payment {} approved by organizer {}", paymentId, organizerId);
        return paymentPersistenceMapper.toModel(paymentRepository.save(payment));
    }

    @Transactional
    public Payment rejectPayment(Long organizerId, Long paymentId, String reason) {
        validateOrganizer(organizerId);

        PaymentEntity payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new UserNotFoundException(paymentId));

        payment.setStatus(PaymentStatus.REJECTED);
        payment.setRejectionReason(reason);
        payment.setReviewedBy(userRepository.findById(organizerId).orElse(null));
        payment.getRegistration().setStatus(RegisterTournamentStatus.RECHAZADO);
        registrationRepository.save(payment.getRegistration());

        log.info("Payment {} rejected by organizer {}", paymentId, organizerId);
        return paymentPersistenceMapper.toModel(paymentRepository.save(payment));
    }

    public List<Payment> getPaymentsByTournament(Long organizerId, Long tournamentId) {
        validateOrganizer(organizerId);
        return paymentRepository.findAllByRegistration_Tournament_Tournament_id(tournamentId)
                .stream()
                .map(paymentPersistenceMapper::toModel)
                .collect(Collectors.toList());
    }

    public List<TournamentRegistration> getRegistrationsByTournament(Long tournamentId) {
        return registrationRepository.findAllByTournament_Tournament_id(tournamentId)
                .stream()
                .map(registrationPersistenceMapper::toModel)
                .collect(Collectors.toList());
    }

    private void validateOrganizer(Long organizerId) {
        UserEntity organizer = userRepository.findById(organizerId)
                .orElseThrow(() -> new UserNotFoundException(organizerId));
        if (organizer.getUserType() != Role.ORGANIZER)
            throw new TournamentValidationException("The user does not have organizer permissions.");
    }
}

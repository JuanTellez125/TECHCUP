package edu.dosw.TECHCUP.core.service;

import edu.dosw.TECHCUP.controller.dto.request.PaymentRequestDTO;
import edu.dosw.TECHCUP.controller.dto.request.TournamentRegistrationRequestDTO;
import edu.dosw.TECHCUP.controller.dto.response.PaymentResponseDTO;
import edu.dosw.TECHCUP.controller.dto.response.TournamentRegistrationResponseDTO;
import edu.dosw.TECHCUP.controller.mapper.PaymentMapper;
import edu.dosw.TECHCUP.controller.mapper.TournamentRegistrationMapper;
import edu.dosw.TECHCUP.core.exception.TournamentNotFoundException;
import edu.dosw.TECHCUP.core.exception.TournamentValidationException;
import edu.dosw.TECHCUP.core.exception.UserNotFoundException;
import edu.dosw.TECHCUP.core.exception.UserValidationException;
import edu.dosw.TECHCUP.core.model.*;
import edu.dosw.TECHCUP.core.model.enums.PaymentStatus;
import edu.dosw.TECHCUP.core.model.enums.RegisterTournamentStatus;
import edu.dosw.TECHCUP.core.model.enums.Role;
import edu.dosw.TECHCUP.core.util.IdGeneratorUtil;
import edu.dosw.TECHCUP.persistence.entity.*;
import edu.dosw.TECHCUP.persistence.mapper.PaymentPersistenceMapper;
import edu.dosw.TECHCUP.persistence.mapper.TournamentRegistrationPersistenceMapper;
import edu.dosw.TECHCUP.persistence.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Base64;
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
    private final PaymentMapper paymentMapper;
    private final TournamentRegistrationMapper registrationMapper;
    private final PaymentPersistenceMapper paymentPersistenceMapper;
    private final TournamentRegistrationPersistenceMapper registrationPersistenceMapper;

    @Transactional
    public TournamentRegistrationResponseDTO registerTeam(Long captainId,
                                                          TournamentRegistrationRequestDTO dto) {
        UserEntity captain = userRepository.findById(captainId)
                .orElseThrow(() -> new UserNotFoundException(captainId));
        if (captain.getUserType() != Role.CAPTAIN)
            throw new UserValidationException("Only the captain can register the team");

        TeamEntity team = teamRepository.findById(dto.getTeamId())
                .orElseThrow(() -> new UserNotFoundException(dto.getTeamId()));

        if (!team.getCaptain().getUser_id().equals(captainId))
            throw new UserValidationException("You are not the captain of this team.");

        TournamentEntity tournament = tournamentRepository.findById(dto.getTournamentId())
                .orElseThrow(() -> new TournamentNotFoundException(dto.getTournamentId()));

        if (registrationRepository.findByTeam_IdAndTournament_Tournament_id(dto.getTeamId(), dto.getTournamentId()).isPresent())
            throw new UserValidationException("The team is already registered for this tournament.");

        TournamentRegistrationEntity registration = TournamentRegistrationEntity.builder()
                .tournament(tournament)
                .team(team)
                .status(RegisterTournamentStatus.PENDIENTE)
                .registeredAt(LocalDateTime.now())
                .build();

        TournamentRegistrationEntity saved = registrationRepository.save(registration);
        log.info("Team {} registered in tournament {}", dto.getTeamId(), dto.getTournamentId());
        return registrationMapper.toDto(registrationPersistenceMapper.toModel(saved));
    }

    @Transactional
    public PaymentResponseDTO submitPayment(Long captainId, PaymentRequestDTO dto) {
        UserEntity captain = userRepository.findById(captainId)
                .orElseThrow(() -> new UserNotFoundException(captainId));
        if (captain.getUserType() != Role.CAPTAIN)
            throw new UserValidationException("Only the captain can upload the receipt.");

        TournamentRegistrationEntity registration = registrationRepository.findById(dto.getRegistrationId())
                .orElseThrow(() -> new UserNotFoundException(dto.getRegistrationId()));

        if (!registration.getTeam().getCaptain().getUser_id().equals(captainId))
            throw new UserValidationException("You are not the captain of this team..");

        if (paymentRepository.findByRegistration_TournamentRegistration_id(dto.getRegistrationId()).isPresent())
            throw new UserValidationException("There is already proof of this registration.");

        registration.setStatus(RegisterTournamentStatus.EN_REVISION);
        registrationRepository.save(registration);

        PaymentEntity payment = PaymentEntity.builder()
                .registration(registration)
                .uploadedBy(captain)
                .fileUrl(dto.getFileUrl())
                .paymentMethod(dto.getPaymentMethod())
                .status(PaymentStatus.IN_REVIEW)
                .build();

        PaymentEntity saved = paymentRepository.save(payment);
        log.info("Proof uploaded by captain {} for registration {}", captainId, dto.getRegistrationId());
        return paymentMapper.toDto(paymentPersistenceMapper.toModel(saved));
    }

    @Transactional
    public PaymentResponseDTO approvePayment(Long organizerId, Long paymentId) {
        validateOrganizer(organizerId);

        PaymentEntity payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new UserNotFoundException(paymentId));

        payment.setStatus(PaymentStatus.APPROVED);
        payment.setReviewedBy(userRepository.findById(organizerId).orElse(null));
        payment.getRegistration().setStatus(RegisterTournamentStatus.APROBADO);
        registrationRepository.save(payment.getRegistration());

        log.info("Payment {} approved by organizer {}", paymentId, organizerId);
        return paymentMapper.toDto(paymentPersistenceMapper.toModel(paymentRepository.save(payment)));
    }

    @Transactional
    public PaymentResponseDTO rejectPayment(Long organizerId, Long paymentId, String reason) {
        validateOrganizer(organizerId);

        PaymentEntity payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new UserNotFoundException(paymentId));

        payment.setStatus(PaymentStatus.REJECTED);
        payment.setRejectionReason(reason);
        payment.setReviewedBy(userRepository.findById(organizerId).orElse(null));
        payment.getRegistration().setStatus(RegisterTournamentStatus.RECHAZADO);
        registrationRepository.save(payment.getRegistration());

        log.info("Payment {} rejected by organizer {}", paymentId, organizerId);
        return paymentMapper.toDto(paymentPersistenceMapper.toModel(paymentRepository.save(payment)));
    }

    public List<PaymentResponseDTO> getPaymentsByTournament(Long organizerId, Long tournamentId) {
        validateOrganizer(organizerId);
        return paymentRepository.findAllByRegistration_Tournament_Tournament_id(tournamentId)
                .stream()
                .map(e -> paymentMapper.toDto(paymentPersistenceMapper.toModel(e)))
                .collect(Collectors.toList());
    }

    public List<TournamentRegistrationResponseDTO> getRegistrationsByTournament(Long tournamentId) {
        return registrationRepository.findAllByTournament_Tournament_id(tournamentId)
                .stream()
                .map(e -> registrationMapper.toDto(registrationPersistenceMapper.toModel(e)))
                .collect(Collectors.toList());
    }

    private void validateOrganizer(Long organizerId) {
        UserEntity organizer = userRepository.findById(organizerId)
                .orElseThrow(() -> new UserNotFoundException(organizerId));
        if (organizer.getUserType() != Role.ORGANIZER)
            throw new TournamentValidationException("The user does not have organizer permissions.");
    }
}

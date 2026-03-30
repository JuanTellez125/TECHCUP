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

    @Transactional
    public TournamentRegistrationResponseDTO registerTeam(Long captainId,
                                                          TournamentRegistrationRequestDTO dto) {
        UserEntity captain = userRepository.findById(captainId)
                .orElseThrow(() -> new UserNotFoundException(captainId));
        if (captain.getUserType() != Role.CAPTAIN)
            throw new UserValidationException("Solo el capitán puede inscribir el equipo.");

        TeamEntity team = teamRepository.findById(String.valueOf(dto.getTeamId()))
                .orElseThrow(() -> new UserNotFoundException(dto.getTeamId()));

        if (!team.getCaptain().getUser_id().equals(captainId))
            throw new UserValidationException("No eres el capitán de este equipo.");

        TournamentEntity tournament = tournamentRepository.findById(dto.getTournamentId())
                .orElseThrow(() -> new TournamentNotFoundException(dto.getTournamentId()));

        if (registrationRepository.findByTeam_IdAndTournament_Tournament_id(dto.getTeamId(), dto.getTournamentId()).isPresent())
            throw new UserValidationException("El equipo ya está inscrito en este torneo.");

        TournamentRegistrationEntity registration = TournamentRegistrationEntity.builder()
                .tournament(tournament)
                .team(team)
                .status(RegisterTournamentStatus.PENDIENTE)
                .registeredAt(LocalDateTime.now())
                .build();

        TournamentRegistrationEntity saved = registrationRepository.save(registration);
        log.info("Equipo {} inscrito en torneo {}", dto.getTeamId(), dto.getTournamentId());
        return registrationMapper.toDto(saved);
    }

    @Transactional
    public PaymentResponseDTO submitPayment(Long captainId, PaymentRequestDTO dto) {
        UserEntity captain = userRepository.findById(captainId)
                .orElseThrow(() -> new UserNotFoundException(captainId));
        if (captain.getUserType() != Role.CAPTAIN)
            throw new UserValidationException("Solo el capitán puede subir el comprobante.");

        TournamentRegistrationEntity registration = registrationRepository.findById(dto.getRegistrationId())
                .orElseThrow(() -> new UserNotFoundException(dto.getRegistrationId()));

        if (!registration.getTeam().getCaptain().getUser_id().equals(captainId))
            throw new UserValidationException("No eres el capitán de este equipo.");

        if (paymentRepository.findByRegistration_TournamentRegistration_id(dto.getRegistrationId()).isPresent())
            throw new UserValidationException("Ya existe un comprobante para esta inscripción.");

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
        log.info("Comprobante subido por capitán {} para inscripción {}", captainId, dto.getRegistrationId());
        return paymentMapper.toDto(saved);
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

        log.info("Pago {} aprobado por organizador {}", paymentId, organizerId);
        return paymentMapper.toDto(paymentRepository.save(payment));
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

        log.info("Pago {} rechazado por organizador {}", paymentId, organizerId);
        return paymentMapper.toDto(paymentRepository.save(payment));
    }

    public List<PaymentResponseDTO> getPaymentsByTournament(Long organizerId, Long tournamentId) {
        validateOrganizer(organizerId);
        return paymentRepository.findAllByRegistration_Tournament_Tournament_id(tournamentId)
                .stream().map(paymentMapper::toDto).collect(Collectors.toList());
    }

    public List<TournamentRegistrationResponseDTO> getRegistrationsByTournament(Long tournamentId) {
        return registrationRepository.findAllByTournament_Tournament_id(tournamentId)
                .stream().map(registrationMapper::toDto).collect(Collectors.toList());
    }

    private void validateOrganizer(Long organizerId) {
        UserEntity organizer = userRepository.findById(organizerId)
                .orElseThrow(() -> new UserNotFoundException(organizerId));
        if (organizer.getUserType() != Role.ORGANIZER)
            throw new TournamentValidationException("El usuario no tiene permisos de organizador.");
    }
}

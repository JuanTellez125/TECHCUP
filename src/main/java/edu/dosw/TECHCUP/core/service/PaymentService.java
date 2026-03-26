package edu.dosw.TECHCUP.core.service;

import edu.dosw.TECHCUP.controller.dto.request.PaymentRequestDTO;
import edu.dosw.TECHCUP.controller.dto.response.PaymentResponseDTO;
import edu.dosw.TECHCUP.controller.mapper.PaymentMapper;
import edu.dosw.TECHCUP.core.exception.TournamentNotFoundException;
import edu.dosw.TECHCUP.core.exception.TournamentValidationException;
import edu.dosw.TECHCUP.core.exception.UserNotFoundException;
import edu.dosw.TECHCUP.core.exception.UserValidationException;
import edu.dosw.TECHCUP.core.model.Payment;
import edu.dosw.TECHCUP.core.model.Team;
import edu.dosw.TECHCUP.core.model.Tournament;
import edu.dosw.TECHCUP.core.model.User;
import edu.dosw.TECHCUP.core.model.enums.PaymentStatus;
import edu.dosw.TECHCUP.core.model.enums.Role;
import edu.dosw.TECHCUP.core.repository.PaymentRepository;
import edu.dosw.TECHCUP.core.repository.TeamRepository;
import edu.dosw.TECHCUP.core.repository.TournamentRepository;
import edu.dosw.TECHCUP.core.repository.UserRepository;
import edu.dosw.TECHCUP.core.util.IdGeneratorUtil;
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
    private final PaymentMapper paymentMapper;
    private final TeamRepository teamRepository;
    private final TournamentRepository tournamentRepository;
    private final UserRepository userRepository;

    @Transactional
    public PaymentResponseDTO submitPayment(String captainId, PaymentRequestDTO dto) {
        User captain = userRepository.findById(captainId)
                .orElseThrow(() -> new UserNotFoundException(captainId));

        if (!captain.getRole().equals(Role.CAPTAIN)) {
            throw new UserValidationException("Solo el capitán puede subir el comprobante.");
        }

        Team team = teamRepository.findById(dto.getTeamId())
                .orElseThrow(() -> new UserNotFoundException(dto.getTeamId()));

        if (!team.getCaptainId().equals(captainId)) {
            throw new UserValidationException("No eres el capitán de este equipo.");
        }

        Tournament tournament = tournamentRepository.findById(dto.getTournamentId())
                .orElseThrow(() -> new TournamentNotFoundException(
                        Long.parseLong(dto.getTournamentId())));

        // Verificar si ya existe un pago para este equipo/torneo
        if (paymentRepository.findByTeamIdAndTournamentId(dto.getTeamId(), dto.getTournamentId()).isPresent()) {
            throw new UserValidationException("Ya existe un comprobante para este equipo en el torneo.");
        }

        byte[] voucherBytes = Base64.getDecoder().decode(dto.getPaymentVoucherBase64());

        Payment payment = Payment.builder()
                .id(IdGeneratorUtil.generateId())
                .captainId(captainId)
                .team(team)
                .tournament(tournament)
                .paymentVoucher(voucherBytes)
                .paymentStatus(PaymentStatus.IN_REVIEW)
                .submittedAt(LocalDateTime.now())
                .build();

        Payment saved = paymentRepository.save(payment);
        log.info("Comprobante subido por capitán {} para equipo {}", captainId, dto.getTeamId());
        return paymentMapper.toDto(saved);
    }

    @Transactional
    public PaymentResponseDTO approvePayment(String organizerId, String paymentId) {
        validateOrganizer(organizerId);

        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new UserNotFoundException(paymentId));

        payment.setPaymentStatus(PaymentStatus.APPROVED);
        payment.setReviewedAt(LocalDateTime.now());

        log.info("Pago {} aprobado por organizador {}", paymentId, organizerId);
        return paymentMapper.toDto(paymentRepository.save(payment));
    }

    @Transactional
    public PaymentResponseDTO rejectPayment(String organizerId, String paymentId, String reason) {
        validateOrganizer(organizerId);

        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new UserNotFoundException(paymentId));

        payment.setPaymentStatus(PaymentStatus.REJECTED);
        payment.setReviewedAt(LocalDateTime.now());
        payment.setRejectionNotes(reason);

        log.info("Pago {} rechazado por organizador {}", paymentId, organizerId);
        return paymentMapper.toDto(paymentRepository.save(payment));
    }

    public List<PaymentResponseDTO> getPaymentsByTournament(String organizerId, String tournamentId) {
        validateOrganizer(organizerId);
        return paymentRepository.findAllByTournamentId(tournamentId)
                .stream()
                .map(paymentMapper::toDto)
                .collect(Collectors.toList());
    }

    private void validateOrganizer(String organizerId) {
        User organizer = userRepository.findById(organizerId)
                .orElseThrow(() -> new UserNotFoundException(organizerId));
        if (!organizer.getRole().equals(Role.ORGANIZER)) {
            throw new TournamentValidationException("El usuario no tiene permisos de organizador.");
        }
    }
}

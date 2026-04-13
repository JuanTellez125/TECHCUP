package edu.dosw.TECHCUP.controller;

import edu.dosw.TECHCUP.controller.dto.request.PaymentRequestDTO;
import edu.dosw.TECHCUP.controller.dto.request.TournamentRegistrationRequestDTO;
import edu.dosw.TECHCUP.controller.dto.response.PaymentResponseDTO;
import edu.dosw.TECHCUP.controller.dto.response.TournamentRegistrationResponseDTO;
import edu.dosw.TECHCUP.core.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@Tag(name = "Payment", description = "Payment management and tournament registration")
public class PaymentController {

    private final PaymentService paymentService;

    @PreAuthorize("hasAnyRole('CAPTAIN', 'ADMINISTRATOR')")
    @PostMapping("/register")
    public ResponseEntity<TournamentRegistrationResponseDTO> registerTeam(
            @RequestParam Long captainId,
            @RequestBody TournamentRegistrationRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(paymentService.registerTeam(captainId, dto));
    }

    @PreAuthorize("hasAnyRole('CAPTAIN', 'ADMINISTRATOR')")
    @PostMapping("/submit")
    public ResponseEntity<PaymentResponseDTO> submitPayment(
            @RequestParam Long captainId,
            @RequestBody PaymentRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(paymentService.submitPayment(captainId, dto));
    }

    @PreAuthorize("hasAnyRole('ORGANIZER', 'ADMINISTRATOR')")
    @PatchMapping("/{paymentId}/approve")
    public ResponseEntity<PaymentResponseDTO> approvePayment(
            @RequestParam Long organizerId,
            @PathVariable Long paymentId) {
        return ResponseEntity.ok(paymentService.approvePayment(organizerId, paymentId));
    }

    @PreAuthorize("hasAnyRole('ORGANIZER', 'ADMINISTRATOR')")
    @PatchMapping("/{paymentId}/reject")
    public ResponseEntity<PaymentResponseDTO> rejectPayment(
            @RequestParam Long organizerId,
            @PathVariable Long paymentId,
            @RequestParam String reason) {
        return ResponseEntity.ok(paymentService.rejectPayment(organizerId, paymentId, reason));
    }

    @PreAuthorize("hasAnyRole('ORGANIZER', 'ADMINISTRATOR')")
    @GetMapping("/tournament/{tournamentId}")
    public ResponseEntity<List<PaymentResponseDTO>> getPaymentsByTournament(
            @RequestParam Long organizerId,
            @PathVariable Long tournamentId) {
        return ResponseEntity.ok(paymentService.getPaymentsByTournament(organizerId, tournamentId));
    }

    @PreAuthorize("hasAnyRole('ORGANIZER', 'ADMINISTRATOR', 'REFEREE', 'CAPTAIN', 'PLAYER')")
    @GetMapping("/registrations/tournament/{tournamentId}")
    public ResponseEntity<List<TournamentRegistrationResponseDTO>> getRegistrationsByTournament(
            @PathVariable Long tournamentId) {
        return ResponseEntity.ok(paymentService.getRegistrationsByTournament(tournamentId));
    }
}

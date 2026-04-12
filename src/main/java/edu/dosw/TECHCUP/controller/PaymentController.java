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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@Tag(name = "Payment", description = "Payment management and tournament registration")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/register")
    @Operation(summary = "Register team for tournament (captain only)")
    public ResponseEntity<TournamentRegistrationResponseDTO> registerTeam(
            @RequestParam Long captainId,
            @RequestBody TournamentRegistrationRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(paymentService.registerTeam(captainId, dto));
    }

    @PostMapping("/submit")
    @Operation(summary = "Upload proof of payment (captain only)")
    public ResponseEntity<PaymentResponseDTO> submitPayment(
            @RequestParam Long captainId,
            @RequestBody PaymentRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(paymentService.submitPayment(captainId, dto));
    }

    @PatchMapping("/{paymentId}/approve")
    @Operation(summary = "Approve payment (organizer only)")
    public ResponseEntity<PaymentResponseDTO> approvePayment(
            @RequestParam Long organizerId,
            @PathVariable Long paymentId) {
        return ResponseEntity.ok(paymentService.approvePayment(organizerId, paymentId));
    }

    @PatchMapping("/{paymentId}/reject")
    @Operation(summary = "Decline payment (organizer only)")
    public ResponseEntity<PaymentResponseDTO> rejectPayment(
            @RequestParam Long organizerId,
            @PathVariable Long paymentId,
            @RequestParam String reason) {
        return ResponseEntity.ok(paymentService.rejectPayment(organizerId, paymentId, reason));
    }

    @GetMapping("/tournament/{tournamentId}")
    @Operation(summary = "Receiving payments from a tournament (organizer only)")
    public ResponseEntity<List<PaymentResponseDTO>> getPaymentsByTournament(
            @RequestParam Long organizerId,
            @PathVariable Long tournamentId) {
        return ResponseEntity.ok(paymentService.getPaymentsByTournament(organizerId, tournamentId));
    }

    @GetMapping("/registrations/tournament/{tournamentId}")
    @Operation(summary = "Obtain tournament registrations")
    public ResponseEntity<List<TournamentRegistrationResponseDTO>> getRegistrationsByTournament(
            @PathVariable Long tournamentId) {
        return ResponseEntity.ok(paymentService.getRegistrationsByTournament(tournamentId));
    }
}

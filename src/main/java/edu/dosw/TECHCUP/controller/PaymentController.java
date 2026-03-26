package edu.dosw.TECHCUP.controller;

import edu.dosw.TECHCUP.controller.dto.request.PaymentRequestDTO;
import edu.dosw.TECHCUP.controller.dto.response.PaymentResponseDTO;
import edu.dosw.TECHCUP.core.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @Operation(summary = "Upload proof of payment")
    @PostMapping
    public ResponseEntity<PaymentResponseDTO> submitPayment(
            @RequestParam String captainId,
            @Valid @RequestBody PaymentRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(paymentService.submitPayment(captainId, dto));
    }

    @Operation(summary = "Approve payment (organizer) → status APPROVED")
    @PatchMapping("/{paymentId}/approve")
    public ResponseEntity<PaymentResponseDTO> approvePayment(
            @PathVariable String paymentId,
            @RequestParam String organizerId) {
        return ResponseEntity.ok(paymentService.approvePayment(organizerId, paymentId));
    }

    @Operation(summary = "Reject payment (organizer) → status REJECTED")
    @PatchMapping("/{paymentId}/reject")
    public ResponseEntity<PaymentResponseDTO> rejectPayment(
            @PathVariable String paymentId,
            @RequestParam String organizerId,
            @RequestParam(required = false) String reason) {
        return ResponseEntity.ok(paymentService.rejectPayment(organizerId, paymentId, reason));
    }

    @Operation(summary = "Listar pagos de un torneo")
    @GetMapping("/tournament/{tournamentId}")
    public ResponseEntity<List<PaymentResponseDTO>> getPaymentsByTournament(
            @PathVariable String tournamentId,
            @RequestParam String organizerId) {
        return ResponseEntity.ok(paymentService.getPaymentsByTournament(organizerId, tournamentId));
    }
}

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
@Tag(name = "Payment", description = "Gestión de pagos e inscripciones a torneos")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/register")
    @Operation(summary = "Inscribir equipo en torneo (solo capitán)")
    public ResponseEntity<TournamentRegistrationResponseDTO> registerTeam(
            @RequestParam Long captainId,
            @RequestBody TournamentRegistrationRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(paymentService.registerTeam(captainId, dto));
    }

    @PostMapping("/submit")
    @Operation(summary = "Subir comprobante de pago (solo capitán)")
    public ResponseEntity<PaymentResponseDTO> submitPayment(
            @RequestParam Long captainId,
            @RequestBody PaymentRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(paymentService.submitPayment(captainId, dto));
    }

    @PatchMapping("/{paymentId}/approve")
    @Operation(summary = "Aprobar pago (solo organizador)")
    public ResponseEntity<PaymentResponseDTO> approvePayment(
            @RequestParam Long organizerId,
            @PathVariable Long paymentId) {
        return ResponseEntity.ok(paymentService.approvePayment(organizerId, paymentId));
    }

    @PatchMapping("/{paymentId}/reject")
    @Operation(summary = "Rechazar pago (solo organizador)")
    public ResponseEntity<PaymentResponseDTO> rejectPayment(
            @RequestParam Long organizerId,
            @PathVariable Long paymentId,
            @RequestParam String reason) {
        return ResponseEntity.ok(paymentService.rejectPayment(organizerId, paymentId, reason));
    }

    @GetMapping("/tournament/{tournamentId}")
    @Operation(summary = "Obtener pagos de un torneo (solo organizador)")
    public ResponseEntity<List<PaymentResponseDTO>> getPaymentsByTournament(
            @RequestParam Long organizerId,
            @PathVariable Long tournamentId) {
        return ResponseEntity.ok(paymentService.getPaymentsByTournament(organizerId, tournamentId));
    }

    @GetMapping("/registrations/tournament/{tournamentId}")
    @Operation(summary = "Obtener inscripciones de un torneo")
    public ResponseEntity<List<TournamentRegistrationResponseDTO>> getRegistrationsByTournament(
            @PathVariable Long tournamentId) {
        return ResponseEntity.ok(paymentService.getRegistrationsByTournament(tournamentId));
    }
}

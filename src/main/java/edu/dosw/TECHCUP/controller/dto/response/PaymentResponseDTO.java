package edu.dosw.TECHCUP.controller.dto.response;

import edu.dosw.TECHCUP.core.model.enums.PaymentStatus;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponseDTO {

    private String id;

    private String captainId;

    private String teamId;

    private String teamName;

    private String tournamentId;

    private PaymentStatus paymentStatus;

    private LocalDateTime submittedAt;

    private LocalDateTime reviewedAt;

    private String rejectionNotes;
}

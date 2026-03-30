package edu.dosw.TECHCUP.controller.dto.response;

import edu.dosw.TECHCUP.core.model.enums.PaymentStatus;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponseDTO {

    private Long id;

    private Long registrationId;

    private String fileUrl;

    private String paymentMethod;

    private PaymentStatus status;

    private String rejectionReason;

    private LocalDateTime submittedAt;

    private LocalDateTime reviewedAt;
}
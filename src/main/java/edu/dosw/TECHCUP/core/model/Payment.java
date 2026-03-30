package edu.dosw.TECHCUP.core.model;

import edu.dosw.TECHCUP.core.model.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Payment {
    private Long payment_id;
    private TournamentRegistration registration;
    private User uploadedBy;
    private User reviewedBy;
    private String fileUrl;
    private String paymentMethod;
    private PaymentStatus status;
    private String rejectionReason;
}

package edu.dosw.TECHCUP.core.model;

import edu.dosw.TECHCUP.core.model.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Payment {
    private Long paymentId;
    private TournamentRegistration registration;
    private User uploadedBy;
    private User reviewedBy;
    private String fileUrl;
    private String paymentMethod;
    private PaymentStatus status;
    private String rejectionReason;
}
package edu.dosw.TECHCUP.controller.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequestDTO {

    @NotNull(message = "The registration ID cannot be empty.")
    private Long registrationId;

    @NotBlank(message = "The receipt URL cannot be empty.")
    private String fileUrl;

    @NotBlank(message = "The payment method cannot be empty.")
    private String paymentMethod;
}
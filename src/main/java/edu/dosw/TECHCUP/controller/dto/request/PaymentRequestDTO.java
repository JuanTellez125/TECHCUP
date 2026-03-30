package edu.dosw.TECHCUP.controller.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequestDTO {

    @NotNull(message = "El ID de la inscripción no puede estar vacío")
    private Long registrationId;

    @NotBlank(message = "La URL del comprobante no puede estar vacía")
    private String fileUrl;

    @NotBlank(message = "El método de pago no puede estar vacío")
    private String paymentMethod;
}
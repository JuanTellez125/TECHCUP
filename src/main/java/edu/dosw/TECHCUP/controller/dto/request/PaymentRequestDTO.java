package edu.dosw.TECHCUP.controller.dto.request;


import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequestDTO {

    @NotNull
    private String teamId;

    @NotNull
    private String tournamentId;

    @NotNull
    private String paymentVoucherBase64;
}

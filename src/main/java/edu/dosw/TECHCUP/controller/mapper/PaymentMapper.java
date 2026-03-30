package edu.dosw.TECHCUP.controller.mapper;

import edu.dosw.TECHCUP.controller.dto.response.PaymentResponseDTO;
import edu.dosw.TECHCUP.core.model.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    @Mapping(source = "payment_id",                    target = "id")
    @Mapping(source = "registration.TournamentRegistration_id", target = "registrationId")
    PaymentResponseDTO toDto(Payment payment);
}
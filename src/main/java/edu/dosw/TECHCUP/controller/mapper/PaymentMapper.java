package edu.dosw.TECHCUP.controller.mapper;

import edu.dosw.TECHCUP.controller.dto.request.PaymentRequestDTO;
import edu.dosw.TECHCUP.controller.dto.response.PaymentResponseDTO;
import edu.dosw.TECHCUP.core.model.Payment;
import edu.dosw.TECHCUP.persistence.entity.PaymentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    @Mapping(source = "paymentId",                               target = "id")
    @Mapping(source = "registration.tournamentRegistrationId",   target = "registrationId")
    PaymentResponseDTO toDto(Payment payment);

    @Mapping(target = "registration.tournamentRegistrationId", source = "registrationId")
    Payment toModel(PaymentRequestDTO dto);
}
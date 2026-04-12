package edu.dosw.TECHCUP.persistence.mapper;

import edu.dosw.TECHCUP.controller.dto.response.PaymentResponseDTO;
import edu.dosw.TECHCUP.core.model.Payment;
import edu.dosw.TECHCUP.persistence.entity.PaymentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PaymentPersistenceMapper {

    Payment toModel(PaymentEntity payment);

    PaymentEntity toEntity(Payment payment);
}
package edu.dosw.TECHCUP.persistence.mapper;

import edu.dosw.TECHCUP.core.model.Payment;
import edu.dosw.TECHCUP.persistence.entity.PaymentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {TournamentRegistrationPersistenceMapper.class, UserPersistenceMapper.class})
public interface PaymentPersistenceMapper {

    @Mapping(source = "payment_id", target = "paymentId")
    Payment toModel(PaymentEntity payment);

    @Mapping(source = "paymentId", target = "payment_id")
    PaymentEntity toEntity(Payment payment);
}
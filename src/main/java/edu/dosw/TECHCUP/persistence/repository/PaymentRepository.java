package edu.dosw.TECHCUP.persistence.repository;

import edu.dosw.TECHCUP.core.model.enums.PaymentStatus;
import edu.dosw.TECHCUP.persistence.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {

    Optional<PaymentEntity> findByRegistration_TournamentRegistration_id(Long registrationId);

    List<PaymentEntity> findAllByRegistration_Tournament_Tournament_id(Long tournamentId);

    List<PaymentEntity> findAllByRegistration_Tournament_Tournament_idAndStatus(Long tournamentId, PaymentStatus status);
}

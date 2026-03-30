package edu.dosw.TECHCUP.persistence.repository;

import edu.dosw.TECHCUP.core.model.Payment;
import edu.dosw.TECHCUP.core.model.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByRegistration_TournamentRegistration_id(Long registrationId);

    List<Payment> findAllByRegistration_Tournament_Tournament_id(Long tournamentId);

    List<Payment> findAllByRegistration_Tournament_Tournament_idAndStatus(Long tournamentId, PaymentStatus status);
}
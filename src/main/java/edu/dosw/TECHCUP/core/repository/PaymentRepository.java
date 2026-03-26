package edu.dosw.TECHCUP.core.repository;

import edu.dosw.TECHCUP.core.model.Payment;
import edu.dosw.TECHCUP.core.model.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, String> {

    Optional<Payment> findByTeamIdAndTournamentId(String teamId, String tournamentId);

    List<Payment> findAllByTournamentId(String tournamentId);

    List<Payment> findAllByTournamentIdAndPaymentStatus(String tournamentId, PaymentStatus status);
}
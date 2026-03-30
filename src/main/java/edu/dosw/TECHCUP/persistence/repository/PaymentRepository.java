package edu.dosw.TECHCUP.persistence.repository;

import edu.dosw.TECHCUP.core.model.enums.PaymentStatus;
import edu.dosw.TECHCUP.persistence.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {

    @Query("SELECT p FROM PaymentEntity p WHERE p.registration.tournamentRegistration_id = :registrationId")
    Optional<PaymentEntity> findByRegistration_TournamentRegistration_id(@Param("registrationId") Long registrationId);

    @Query("SELECT p FROM PaymentEntity p WHERE p.registration.tournament.tournament_id = :tournamentId")
    List<PaymentEntity> findAllByRegistration_Tournament_Tournament_id(@Param("tournamentId") Long tournamentId);

    @Query("SELECT p FROM PaymentEntity p WHERE p.registration.tournament.tournament_id = :tournamentId AND p.status = :status")
    List<PaymentEntity> findAllByRegistration_Tournament_Tournament_idAndStatus(@Param("tournamentId") Long tournamentId, @Param("status") PaymentStatus status);
}
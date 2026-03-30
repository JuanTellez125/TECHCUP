package edu.dosw.TECHCUP.persistence.repository;

import edu.dosw.TECHCUP.core.model.TournamentRegistration;
import edu.dosw.TECHCUP.core.model.enums.RegisterTournamentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TournamentRegistrationRepository extends JpaRepository<TournamentRegistration, Long> {

    Optional<TournamentRegistration> findByTeam_IdAndTournament_Tournament_id(Long teamId, Long tournamentId);

    List<TournamentRegistration> findAllByTournament_Tournament_id(Long tournamentId);

    List<TournamentRegistration> findAllByTournament_Tournament_idAndStatus(Long tournamentId, RegisterTournamentStatus status);
}
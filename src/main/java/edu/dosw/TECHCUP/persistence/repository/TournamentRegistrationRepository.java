package edu.dosw.TECHCUP.persistence.repository;

import edu.dosw.TECHCUP.core.model.enums.RegisterTournamentStatus;
import edu.dosw.TECHCUP.persistence.entity.TournamentRegistrationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TournamentRegistrationRepository extends JpaRepository<TournamentRegistrationEntity, Long> {

    Optional<TournamentRegistrationEntity> findByTeam_IdAndTournament_Tournament_id(Long teamId, Long tournamentId);

    List<TournamentRegistrationEntity> findAllByTournament_Tournament_id(Long tournamentId);

    List<TournamentRegistrationEntity> findAllByTournament_Tournament_idAndStatus(Long tournamentId, RegisterTournamentStatus status);
}

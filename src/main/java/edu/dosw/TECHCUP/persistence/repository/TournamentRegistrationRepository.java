package edu.dosw.TECHCUP.persistence.repository;

import edu.dosw.TECHCUP.core.model.enums.RegisterTournamentStatus;
import edu.dosw.TECHCUP.persistence.entity.TournamentRegistrationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TournamentRegistrationRepository extends JpaRepository<TournamentRegistrationEntity, Long> {

    @Query("SELECT r FROM TournamentRegistrationEntity r WHERE r.team.id = :teamId AND r.tournament.tournament_id = :tournamentId")
    Optional<TournamentRegistrationEntity> findByTeam_IdAndTournament_Tournament_id(@Param("teamId") Long teamId, @Param("tournamentId") Long tournamentId);

    @Query("SELECT r FROM TournamentRegistrationEntity r WHERE r.tournament.tournament_id = :tournamentId")
    List<TournamentRegistrationEntity> findAllByTournament_Tournament_id(@Param("tournamentId") Long tournamentId);

    @Query("SELECT r FROM TournamentRegistrationEntity r WHERE r.tournament.tournament_id = :tournamentId AND r.status = :status")
    List<TournamentRegistrationEntity> findAllByTournament_Tournament_idAndStatus(@Param("tournamentId") Long tournamentId, @Param("status") RegisterTournamentStatus status);
}
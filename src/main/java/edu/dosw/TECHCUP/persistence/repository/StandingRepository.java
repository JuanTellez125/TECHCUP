package edu.dosw.TECHCUP.persistence.repository;

import edu.dosw.TECHCUP.persistence.entity.StandingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StandingRepository extends JpaRepository<StandingEntity, Long> {

    @Query("SELECT s FROM StandingEntity s WHERE s.tournament.tournament_id = :tournamentId ORDER BY s.points DESC")
    List<StandingEntity> findAllByTournament_Tournament_idOrderByPointsDesc(@Param("tournamentId") Long tournamentId);

    @Query("SELECT s FROM StandingEntity s WHERE s.tournament.tournament_id = :tournamentId AND s.team.id = :teamId")
    Optional<StandingEntity> findByTournament_Tournament_idAndTeam_Id(@Param("tournamentId") Long tournamentId, @Param("teamId") Long teamId);
}
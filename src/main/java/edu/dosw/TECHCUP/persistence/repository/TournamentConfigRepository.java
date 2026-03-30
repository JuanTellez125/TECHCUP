package edu.dosw.TECHCUP.persistence.repository;

import edu.dosw.TECHCUP.persistence.entity.TournamentConfigEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TournamentConfigRepository extends JpaRepository<TournamentConfigEntity, Long> {

    @Query("SELECT c FROM TournamentConfigEntity c WHERE c.tournament.tournament_id = :tournamentId")
    Optional<TournamentConfigEntity> findByTournament_Tournament_id(@Param("tournamentId") Long tournamentId);
}
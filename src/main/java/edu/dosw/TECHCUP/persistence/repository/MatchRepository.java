package edu.dosw.TECHCUP.persistence.repository;

import edu.dosw.TECHCUP.core.model.enums.MatchPhase;
import edu.dosw.TECHCUP.persistence.entity.MatchEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchRepository extends JpaRepository<MatchEntity, Long> {

    @Query("SELECT m FROM MatchEntity m WHERE m.tournament.tournament_id = :tournamentId")
    List<MatchEntity> findAllByTournament_Tournament_id(@Param("tournamentId") Long tournamentId);

    @Query("SELECT m FROM MatchEntity m WHERE m.tournament.tournament_id = :tournamentId AND m.phase = :phase")
    List<MatchEntity> findAllByTournament_Tournament_idAndPhase(@Param("tournamentId") Long tournamentId, @Param("phase") MatchPhase phase);

    @Query("SELECT m FROM MatchEntity m WHERE m.referee.user_id = :refereeId")
    List<MatchEntity> findAllByReferee_User_id(@Param("refereeId") Long refereeId);

    @Query("SELECT m FROM MatchEntity m WHERE m.team1.id = :team1Id OR m.team2.id = :team2Id")
    List<MatchEntity> findAllByTeam1_IdOrTeam2_Id(@Param("team1Id") Long team1Id, @Param("team2Id") Long team2Id);
}
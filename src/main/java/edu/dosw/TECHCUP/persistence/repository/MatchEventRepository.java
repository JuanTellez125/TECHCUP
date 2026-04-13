package edu.dosw.TECHCUP.persistence.repository;

import edu.dosw.TECHCUP.core.model.enums.Event;
import edu.dosw.TECHCUP.persistence.entity.MatchEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchEventRepository extends JpaRepository<MatchEventEntity, Long> {

    @Query("SELECT e FROM MatchEventEntity e WHERE e.match.match_id = :matchId")
    List<MatchEventEntity> findAllByMatch_Match_id(@Param("matchId") Long matchId);

    @Query("SELECT e FROM MatchEventEntity e WHERE e.match.match_id = :matchId AND e.eventType = :eventType")
    List<MatchEventEntity> findAllByMatch_Match_idAndEventType(@Param("matchId") Long matchId, @Param("eventType") Event eventType);

    @Query("SELECT e FROM MatchEventEntity e WHERE e.eventType = :eventType AND e.match.tournament.tournament_id = :tournamentId")
    List<MatchEventEntity> findAllByEventTypeAndMatch_Tournament_Tournament_id(@Param("eventType") Event eventType, @Param("tournamentId") Long tournamentId);

    @Query("SELECT e.user.user_id, e.user.firstName, e.user.lastName, e.team.id, e.team.name, COUNT(e) " +
            "FROM MatchEventEntity e " +
            "WHERE e.match.tournament.tournament_id = :tournamentId AND e.eventType = 'GOL' " +
            "GROUP BY e.user.user_id, e.user.firstName, e.user.lastName, e.team.id, e.team.name " +
            "ORDER BY COUNT(e) DESC")
    List<Object[]> findTopScorersByTournament(@Param("tournamentId") Long tournamentId);
}
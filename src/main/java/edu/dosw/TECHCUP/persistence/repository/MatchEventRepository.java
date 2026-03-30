package edu.dosw.TECHCUP.persistence.repository;

import edu.dosw.TECHCUP.core.model.enums.Event;
import edu.dosw.TECHCUP.persistence.entity.MatchEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchEventRepository extends JpaRepository<MatchEventEntity, Long> {

    List<MatchEventEntity> findAllByMatch_Match_id(Long matchId);

    List<MatchEventEntity> findAllByMatch_Match_idAndEventType(Long matchId, Event eventType);

    List<MatchEventEntity> findAllByEventTypeAndMatch_Tournament_Tournament_id(Event eventType, Long tournamentId);
}

package edu.dosw.TECHCUP.persistence.repository;

import edu.dosw.TECHCUP.core.model.enums.MatchPhase;
import edu.dosw.TECHCUP.persistence.entity.MatchEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchRepository extends JpaRepository<MatchEntity, Long> {

    List<MatchEntity> findAllByTournament_Tournament_id(Long tournamentId);

    List<MatchEntity> findAllByTournament_Tournament_idAndPhase(Long tournamentId, MatchPhase phase);

    List<MatchEntity> findAllByReferee_User_id(Long refereeId);

    List<MatchEntity> findAllByTeam1_IdOrTeam2_Id(Long team1Id, Long team2Id);
}

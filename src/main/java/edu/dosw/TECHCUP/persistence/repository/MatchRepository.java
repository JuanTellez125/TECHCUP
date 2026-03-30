package edu.dosw.TECHCUP.persistence.repository;

import edu.dosw.TECHCUP.core.model.Match;
import edu.dosw.TECHCUP.core.model.User;
import edu.dosw.TECHCUP.core.model.enums.MatchPhase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;
import java.util.List;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {

    List<Match> findAllByTournament_Tournament_id(Long tournamentId);

    List<Match> findAllByTournament_Tournament_idAndPhase(Long tournamentId, MatchPhase phase);

    List<Match> findAllByRefereeId(Long refereeId);

    List<Match> findAllByTeam1_IdOrTeam2_Id(Long team1Id, Long team2Id);


    List<Match> findAllByReferee_User_id(Long refereeId);
}
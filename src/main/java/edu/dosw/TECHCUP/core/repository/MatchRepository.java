package edu.dosw.TECHCUP.core.repository;

import edu.dosw.TECHCUP.core.model.Match;
import edu.dosw.TECHCUP.core.model.enums.MatchPhase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchRepository extends JpaRepository<Match, String> {

    List<Match> findAllByTournamentId(String tournamentId);

    List<Match> findAllByTournamentIdAndMatchPhase(String tournamentId, MatchPhase phase);

    List<Match> findAllByRefereeId(String refereeId);

    List<Match> findAllByLocalTeamIdOrAwayTeamId(String localTeamId, String awayTeamId);

    List<Match> findAllByTournamentIdAndIsPlayed(String tournamentId, boolean isPlayed);
}

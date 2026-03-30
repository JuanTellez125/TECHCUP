package edu.dosw.TECHCUP.persistence.repository;

import edu.dosw.TECHCUP.core.model.Team;
import edu.dosw.TECHCUP.core.model.enums.TeamStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamRepository extends JpaRepository<Team, String> {

    Optional<Team> findByCaptainId(String captainId);

    List<Team> findAllByTournamentId(String tournamentId);

    List<Team> findAllByTeamStatus(TeamStatus status);

    boolean existsByTeamName(String teamName);

    List<Team> findAllByTournamentIdAndTeamStatus(String tournamentId, TeamStatus status);

    boolean existsByName(String name);
}
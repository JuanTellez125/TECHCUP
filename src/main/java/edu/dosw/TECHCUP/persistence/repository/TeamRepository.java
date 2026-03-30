package edu.dosw.TECHCUP.persistence.repository;

import edu.dosw.TECHCUP.core.model.Team;
import edu.dosw.TECHCUP.core.model.enums.TeamStatus;
import edu.dosw.TECHCUP.persistence.entity.TeamEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamRepository extends JpaRepository<TeamEntity, String> {

    Optional<TeamEntity> findByCaptainId(String captainId);

    List<TeamEntity> findAllByTournamentId(String tournamentId);

    List<TeamEntity> findAllByTeamStatus(TeamStatus status);

    boolean existsByTeamName(String teamName);

    List<TeamEntity> findAllByTournamentIdAndTeamStatus(String tournamentId, TeamStatus status);

    boolean existsByName(String name);
}
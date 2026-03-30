package edu.dosw.TECHCUP.persistence.repository;

import edu.dosw.TECHCUP.core.model.Standing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StandingRepository extends JpaRepository<Standing, Long> {

    List<Standing> findAllByTournament_Tournament_idOrderByPointsDesc(Long tournamentId);

    Optional<Standing> findByTournament_Tournament_idAndTeam_Id(Long tournamentId, Long teamId);
}
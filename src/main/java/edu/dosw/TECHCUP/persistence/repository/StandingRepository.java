package edu.dosw.TECHCUP.persistence.repository;

import edu.dosw.TECHCUP.core.model.Standing;
import edu.dosw.TECHCUP.persistence.entity.StandingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StandingRepository extends JpaRepository<StandingEntity, Long> {

    List<StandingEntity> findAllByTournament_Tournament_idOrderByPointsDesc(Long tournamentId);

    Optional<StandingEntity> findByTournament_Tournament_idAndTeam_Id(Long tournamentId, Long teamId);
}
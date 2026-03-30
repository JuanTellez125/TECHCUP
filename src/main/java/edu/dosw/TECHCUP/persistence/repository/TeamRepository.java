package edu.dosw.TECHCUP.persistence.repository;

import edu.dosw.TECHCUP.persistence.entity.TeamEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamRepository extends JpaRepository<TeamEntity, Long> {


    Optional<TeamEntity> findByCaptain_User_id(Long captainId);

    boolean existsByName(String name);

    List<TeamEntity> findAllByActiveTrue();

    List<TeamEntity> findAllByTournamentId(Long tournamentId);
}

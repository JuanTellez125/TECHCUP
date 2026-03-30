package edu.dosw.TECHCUP.persistence.repository;

import edu.dosw.TECHCUP.core.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {

    Optional<Team> findByCaptain_User_id(Long captainId);

    boolean existsByName(String name);
}
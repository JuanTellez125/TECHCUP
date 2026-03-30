package edu.dosw.TECHCUP.persistence.repository;

import edu.dosw.TECHCUP.core.model.TournamentConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TournamentConfigRepository extends JpaRepository<TournamentConfig, Long> {

    Optional<TournamentConfig> findByTournament_Tournament_id(Long tournamentId);
}
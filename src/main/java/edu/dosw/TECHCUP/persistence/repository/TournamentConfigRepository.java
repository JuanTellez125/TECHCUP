package edu.dosw.TECHCUP.persistence.repository;

import edu.dosw.TECHCUP.core.model.TournamentConfig;
import edu.dosw.TECHCUP.persistence.entity.TournamentConfigEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TournamentConfigRepository extends JpaRepository<TournamentConfigEntity, Long> {

    Optional<TournamentConfigEntity> findByTournament_Tournament_id(Long tournamentId);
}
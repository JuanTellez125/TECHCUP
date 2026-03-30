package edu.dosw.TECHCUP.persistence.repository;

import edu.dosw.TECHCUP.core.model.enums.TournamentStatus;
import edu.dosw.TECHCUP.persistence.entity.TournamentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TournamentRepository extends JpaRepository<TournamentEntity, Long> {

    Optional<TournamentEntity> findByStatus(TournamentStatus status);

    List<TournamentEntity> findAllByStatus(TournamentStatus status);
}

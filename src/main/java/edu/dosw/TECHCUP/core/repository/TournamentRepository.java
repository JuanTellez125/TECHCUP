package edu.dosw.TECHCUP.core.repository;

import edu.dosw.TECHCUP.core.model.Tournament;
import edu.dosw.TECHCUP.core.model.enums.TournamentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TournamentRepository extends JpaRepository<Tournament, String> {

    Optional<Tournament> findByStatus(TournamentStatus status);

    Optional<Tournament> findById(String id);

    List<Tournament> findAll();



}

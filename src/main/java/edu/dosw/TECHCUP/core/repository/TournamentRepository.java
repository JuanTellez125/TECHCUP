package edu.dosw.TECHCUP.core.repository;

import edu.dosw.TECHCUP.core.model.Tournament;
import edu.dosw.TECHCUP.core.model.TournamentStatus;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface TournamentRepository extends MongoRepository<Tournament, String> {

    Optional<Tournament> findByStatus(TournamentStatus status);

    Optional<Tournament> findById(String id);

    List<Tournament> findAll();



}

package edu.dosw.TECHCUP.persistence.repository;

import edu.dosw.TECHCUP.core.model.Venue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VenueRepository extends JpaRepository<Venue, Long> {

    List<Venue> findAllByTournament_Tournament_id(Long tournamentId);
}
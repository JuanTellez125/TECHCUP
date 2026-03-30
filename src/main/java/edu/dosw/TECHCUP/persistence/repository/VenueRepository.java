package edu.dosw.TECHCUP.persistence.repository;

import edu.dosw.TECHCUP.core.model.Venue;
import edu.dosw.TECHCUP.persistence.entity.VenueEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VenueRepository extends JpaRepository<VenueEntity, Long> {

    List<VenueEntity> findAllByTournament_Tournament_id(Long tournamentId);
}
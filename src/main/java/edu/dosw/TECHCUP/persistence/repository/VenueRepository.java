package edu.dosw.TECHCUP.persistence.repository;

import edu.dosw.TECHCUP.persistence.entity.VenueEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VenueRepository extends JpaRepository<VenueEntity, Long> {

    @Query("SELECT v FROM VenueEntity v WHERE v.tournament.tournament_id = :tournamentId")
    List<VenueEntity> findAllByTournament_Tournament_id(@Param("tournamentId") Long tournamentId);
}
package edu.dosw.TECHCUP.persistence.repository;

import edu.dosw.TECHCUP.persistence.entity.MatchResultEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MatchResultRepository extends JpaRepository<MatchResultEntity, Long> {

    @Query("SELECT r FROM MatchResultEntity r WHERE r.match.match_id = :matchId")
    Optional<MatchResultEntity> findByMatch_Match_id(@Param("matchId") Long matchId);
}
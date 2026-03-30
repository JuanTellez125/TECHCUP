package edu.dosw.TECHCUP.persistence.repository;

import edu.dosw.TECHCUP.core.model.MatchResult;
import edu.dosw.TECHCUP.persistence.entity.MatchResultEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MatchResultRepository extends JpaRepository<MatchResultEntity, Long> {

    Optional<MatchResultEntity> findByMatch_Match_id(Long matchId);
}
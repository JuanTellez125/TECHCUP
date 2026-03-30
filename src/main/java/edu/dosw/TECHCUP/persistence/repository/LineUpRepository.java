package edu.dosw.TECHCUP.persistence.repository;

import edu.dosw.TECHCUP.persistence.entity.LineUpEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LineUpRepository extends JpaRepository<LineUpEntity, Long> {

    List<LineUpEntity> findAllByMatch_Match_idAndTeam_Id(Long matchId, Long teamId);

    List<LineUpEntity> findAllByMatch_Match_id(Long matchId);

    @Query("SELECT l FROM LineUpEntity l WHERE l.match.match_id = :matchId AND l.team.id = :teamId")
    Optional<LineUpEntity> findByMatchAndTeam(@Param("matchId") Long matchId, @Param("teamId") Long teamId);
}

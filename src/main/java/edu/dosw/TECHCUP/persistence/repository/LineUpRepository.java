package edu.dosw.TECHCUP.persistence.repository;

import edu.dosw.TECHCUP.core.model.LineUp;
import edu.dosw.TECHCUP.persistence.entity.LineUpEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LineUpRepository extends JpaRepository<LineUpEntity, Long> {

    List<LineUpEntity> findAllByMatch_Match_idAndTeam_Id(Long matchId, Long teamId);

    List<LineUpEntity> findAllByMatch_Match_id(Long matchId);

    Optional<LineUpEntity> findByTeamIdAndMatchId(Long teamId, Long matchId);
}
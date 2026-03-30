package edu.dosw.TECHCUP.persistence.repository;

import edu.dosw.TECHCUP.core.model.LineUp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LineUpRepository extends JpaRepository<LineUp, Long> {

    List<LineUp> findAllByMatch_Match_idAndTeam_Id(Long matchId, Long teamId);

    List<LineUp> findAllByMatch_Match_id(Long matchId);

    Optional<LineUp> findByTeamIdAndMatchId(Long teamId, Long matchId);
}
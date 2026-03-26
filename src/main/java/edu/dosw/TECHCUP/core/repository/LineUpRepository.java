package edu.dosw.TECHCUP.core.repository;

import edu.dosw.TECHCUP.core.model.LineUp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LineUpRepository extends JpaRepository<LineUp, String> {

    Optional<LineUp> findByTeamIdAndMatchId(String teamId, String matchId);

    List<LineUp> findAllByTeamId(String teamId);
}

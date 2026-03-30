package edu.dosw.TECHCUP.persistence.repository;

import edu.dosw.TECHCUP.persistence.entity.TeamEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamRepository extends JpaRepository<TeamEntity, Long> {

    @Query("SELECT t FROM TeamEntity t WHERE t.captain.user_id = :captainId")
    Optional<TeamEntity> findByCaptain_User_id(@Param("captainId") Long captainId);

    boolean existsByName(String name);

    List<TeamEntity> findAllByActiveTrue();
}
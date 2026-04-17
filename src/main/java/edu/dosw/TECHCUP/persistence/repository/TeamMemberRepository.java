package edu.dosw.TECHCUP.persistence.repository;

import edu.dosw.TECHCUP.persistence.entity.TeamMemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamMemberRepository extends JpaRepository<TeamMemberEntity, Long> {

    List<TeamMemberEntity> findAllByTeam_Id(Long teamId);

    @Query("SELECT tm FROM TeamMemberEntity tm WHERE tm.team.id = :teamId AND tm.user.user_id = :userId")
    Optional<TeamMemberEntity> findByTeam_IdAndUser_User_id(@Param("teamId") Long teamId, @Param("userId") Long userId);

    @Query("SELECT COUNT(tm) > 0 FROM TeamMemberEntity tm WHERE tm.team.id = :teamId AND tm.user.user_id = :userId")
    boolean existsByTeam_IdAndUser_User_id(@Param("teamId") Long teamId, @Param("userId") Long userId);

    @Query("SELECT tm FROM TeamMemberEntity tm WHERE tm.user.user_id = :userId AND tm.status = 'ACEPTADO'")
    Optional<TeamMemberEntity> findActiveByUser_User_id(@Param("userId") Long userId);
}
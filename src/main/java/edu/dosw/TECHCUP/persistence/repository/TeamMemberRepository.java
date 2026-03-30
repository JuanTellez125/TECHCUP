package edu.dosw.TECHCUP.persistence.repository;

import edu.dosw.TECHCUP.persistence.entity.TeamMemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamMemberRepository extends JpaRepository<TeamMemberEntity, Long> {

    List<TeamMemberEntity> findAllByTeam_Id(Long teamId);

    Optional<TeamMemberEntity> findByTeam_IdAndUser_User_id(Long teamId, Long userId);

    boolean existsByTeam_IdAndUser_User_id(Long teamId, Long userId);
}

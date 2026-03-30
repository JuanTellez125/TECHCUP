package edu.dosw.TECHCUP.persistence.repository;

import edu.dosw.TECHCUP.core.model.TeamMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamMemberRepository extends JpaRepository<TeamMember, Long> {

    List<TeamMember> findAllByTeam_Id(Long teamId);

    Optional<TeamMember> findByTeam_IdAndUser_User_id(Long teamId, Long userId);

    boolean existsByTeam_IdAndUser_User_id(Long teamId, Long userId);
}
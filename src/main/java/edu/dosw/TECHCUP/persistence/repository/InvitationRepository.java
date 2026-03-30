package edu.dosw.TECHCUP.persistence.repository;

import edu.dosw.TECHCUP.core.model.Invitation;
import edu.dosw.TECHCUP.core.model.enums.InvitationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvitationRepository extends JpaRepository<Invitation, Long> {

    List<Invitation> findAllByInvitedUser_User_id(Long userId);

    List<Invitation> findAllByTeam_Id(Long teamId);

    List<Invitation> findAllByInvitedUser_User_idAndStatus(Long userId, InvitationStatus status);
}
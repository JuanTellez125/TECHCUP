package edu.dosw.TECHCUP.persistence.repository;

import edu.dosw.TECHCUP.core.model.Invitation;
import edu.dosw.TECHCUP.core.model.enums.InvitationStatus;
import edu.dosw.TECHCUP.persistence.entity.InvitationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvitationRepository extends JpaRepository<InvitationEntity, Long> {

    List<InvitationEntity> findAllByInvitedUser_User_id(Long userId);

    List<InvitationEntity> findAllByTeam_Id(Long teamId);

    List<InvitationEntity> findAllByInvitedUser_User_idAndStatus(Long userId, InvitationStatus status);
}
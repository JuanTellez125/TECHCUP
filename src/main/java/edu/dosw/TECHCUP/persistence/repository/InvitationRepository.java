package edu.dosw.TECHCUP.persistence.repository;

import edu.dosw.TECHCUP.core.model.enums.InvitationStatus;
import edu.dosw.TECHCUP.persistence.entity.InvitationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvitationRepository extends JpaRepository<InvitationEntity, Long> {

    @Query("SELECT i FROM InvitationEntity i WHERE i.invitedUser.user_id = :userId")
    List<InvitationEntity> findAllByInvitedUser_User_id(@Param("userId") Long userId);

    List<InvitationEntity> findAllByTeam_Id(Long teamId);

    @Query("SELECT i FROM InvitationEntity i WHERE i.invitedUser.user_id = :userId AND i.status = :status")
    List<InvitationEntity> findAllByInvitedUser_User_idAndStatus(@Param("userId") Long userId, @Param("status") InvitationStatus status);
}
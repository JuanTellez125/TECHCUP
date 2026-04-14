package edu.dosw.TECHCUP.persistence.repository;

import edu.dosw.TECHCUP.persistence.entity.RoleAuditLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleAuditLogRepository extends JpaRepository<RoleAuditLogEntity, Long> {

    List<RoleAuditLogEntity> findByTargetUserId(Long targetUserId);

    List<RoleAuditLogEntity> findByAdminId(Long adminId);
}

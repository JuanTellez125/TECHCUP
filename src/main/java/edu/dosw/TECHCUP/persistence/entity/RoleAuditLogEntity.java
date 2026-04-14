package edu.dosw.TECHCUP.persistence.entity;

import edu.dosw.TECHCUP.core.model.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "role_audit_logs")
public class RoleAuditLogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long adminId;

    @Column(nullable = false)
    private Long targetUserId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role previousRole;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role newRole;

    @Column(nullable = false)
    private LocalDateTime timestamp;
}

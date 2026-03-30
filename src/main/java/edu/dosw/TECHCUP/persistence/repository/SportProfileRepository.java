package edu.dosw.TECHCUP.persistence.repository;

import edu.dosw.TECHCUP.core.model.SportProfile;
import edu.dosw.TECHCUP.core.model.enums.Position;
import edu.dosw.TECHCUP.persistence.entity.SportProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SportProfileRepository extends JpaRepository<SportProfileEntity, Long> {

    Optional<SportProfileEntity> findByUser_User_id(Long userId);

    List<SportProfileEntity> findAllByAvailableTrue();

    List<SportProfileEntity> findAllByPrimaryPositionAndAvailableTrue(Position position);
}
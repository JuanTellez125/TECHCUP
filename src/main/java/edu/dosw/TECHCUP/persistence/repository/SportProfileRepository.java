package edu.dosw.TECHCUP.persistence.repository;

import edu.dosw.TECHCUP.core.model.SportProfile;
import edu.dosw.TECHCUP.core.model.enums.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SportProfileRepository extends JpaRepository<SportProfile, Long> {

    Optional<SportProfile> findByUser_User_id(Long userId);

    List<SportProfile> findAllByAvailableTrue();

    List<SportProfile> findAllByPrimaryPositionAndAvailableTrue(Position position);
}
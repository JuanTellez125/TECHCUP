package edu.dosw.TECHCUP.core.repository;

import edu.dosw.TECHCUP.core.model.Team;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TeamRepository extends JpaRepository<Team, String> {


}

